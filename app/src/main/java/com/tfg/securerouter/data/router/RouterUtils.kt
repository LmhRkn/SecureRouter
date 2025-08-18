package com.tfg.securerouter.data.router

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.DhcpInfo
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.tfg.securerouter.ContextProvider
import java.net.InetAddress
import java.io.File
import com.jcraft.jsch.HostKey
import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
private fun getSshFingerprint(session: Session, jsch: JSch): String? {
    val hk: HostKey = session.hostKey ?: return null

    val keyBytes: ByteArray = try {
        Base64.getDecoder().decode(hk.key)
    } catch (_: Throwable) {
        try {
            HostKey::class.java.getMethod("getKey").invoke(hk) as ByteArray
        } catch (_: Throwable) {
            return null
        }
    }

    val sha256Bytes = MessageDigest.getInstance("SHA-256").digest(keyBytes)
    val sha256B64 = Base64.getEncoder().encodeToString(sha256Bytes).trimEnd('=')

    return "SHA256:$sha256B64"
}

@SuppressLint("ServiceCast")
fun getRouterIpAddress(): String? {
    val ctx = ContextProvider.get()
    val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val net = cm.activeNetwork ?: return null
    val caps = cm.getNetworkCapabilities(net) ?: return null
    if (!caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return null
    val lp = cm.getLinkProperties(net) ?: return null
    val route = lp.routes.firstOrNull { it.isDefaultRoute && it.gateway is java.net.Inet4Address } ?: return null
    return (route.gateway as java.net.Inet4Address).hostAddress
}


fun getGatewayMacAddress(gatewayIp: String?): String? {
    if (gatewayIp.isNullOrBlank()) return null
    return try {
        val line = java.io.File("/proc/net/arp").readLines().firstOrNull { it.startsWith("$gatewayIp ") }
        val mac = line?.trim()?.split(Regex("\\s+"))?.getOrNull(3)
        normalizeMacOrNull(mac)
    } catch (_: Exception) { null }
}

fun getCurrentBssid(): String? {
    return try {
        val ctx: Context = ContextProvider.get()
        val wifi = ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        val raw = wifi.connectionInfo?.bssid
        normalizeMacOrNull(raw) // ← filtra y normaliza
    } catch (_: Exception) { null }
}

fun getCurrentConnectionIdentifier(): String? {
    val ip = getRouterIpAddress()              // gateway IP (DHCP)
    val macGateway = getGatewayMacAddress(ip)  // MAC del gateway real
    val bssid = getCurrentBssid()              // MAC del AP Wi-Fi
    // Prioriza identificadores más únicos
    return macGateway ?: bssid ?: ip
}

fun getCurrentConnectionIdentifiers(): Set<String> {
    val ip = getRouterIpAddress()?.trim()?.lowercase()
    val macGateway = getGatewayMacAddress(ip)
    val bssid = getCurrentBssid()
    return listOfNotNull(macGateway, bssid, ip).toSet()
}


private val SSID_RX = Regex("""ssid=['"]([^'"]+)['"]""")

fun fetchRouterSsidFor(router: RouterInfo): String? {
    val cmd = """
        list=$(uci -q show wireless | sed -n 's/^\(wireless\.[^.]\+\)=wifi-iface$/\1/p');
        # 1) Preferir AP en LAN
        for s in {'$'}list; do
          m=$(uci -q get {'$'}s.mode 2>/dev/null)
          n=$(uci -q get {'$'}s.network 2>/dev/null)
          x=$(uci -q get {'$'}s.ssid 2>/dev/null)
          if [ "{'$'}m" = "ap" ] && [ "{'$'}n" = "lan" ] && [ -n "{'$'}x" ]; then echo "{'$'}x"; exit; fi
        done
        # 2) Si no, cualquier AP
        for s in {'$'}list; do
          m=$(uci -q get {'$'}s.mode 2>/dev/null)
          x=$(uci -q get {'$'}s.ssid 2>/dev/null)
          if [ "{'$'}m" = "ap" ] && [ -n "{'$'}x" ]; then echo "{'$'}x"; exit; fi
        done
        # 3) Último recurso: primer SSID no vacío
        for s in {'$'}list; do
          x=$(uci -q get {'$'}s.ssid 2>/dev/null)
          [ -n "{'$'}x" ] && { echo "{'$'}x"; exit; }
        done
    """.trimIndent()

    val out = sendCommandEphemeral(router, cmd).trim()
    return out.takeIf { it.isNotEmpty() && !it.startsWith("Error") }
}

/** Devuelve true si el host remoto reporta OpenWrt en /etc/openwrt_release (o /etc/os-release). */
fun isOpenWrtBySsh(host: String, viaVpn: Boolean = false): Boolean {
    val tmp = RouterInfo(
        name = "tmp",
        mac = "00:00:00:00:00:00",
        localIp = host,
        publicIpOrDomain = null,
        isVpn = viaVpn,
        id = -1,
        installerPackage = null,
        labels = emptySet(),
        vpnHost = if (viaVpn) host else null,
        vpnPort = 22,
        sshHostKeyFingerprint = null
    )

    val out = sendCommandEphemeral(
        tmp,
        "cat /etc/openwrt_release 2>/dev/null || cat /etc/os-release 2>/dev/null || echo __NO__"
    ).trim()

    if (out.startsWith("Error") || out == "__NO__" || out.isEmpty()) return false

    val s = out.lowercase()
    Log.d("AAAAAAAAAAAAA", "$s")

    if ("openwrt" in s) return true
    return Regex("""(?m)^\s*(name|id)\s*=\s*"?openwrt"?\s*$""").containsMatchIn(s)
}

suspend fun isOpenWrtNoAuth(ip: String, viaVpn: Boolean = false, timeoutMs: Int = 1500): Boolean =
    withContext(kotlinx.coroutines.Dispatchers.IO) {
        // HTTP HEAD a /cgi-bin/luci
        fun head(urlStr: String): Pair<Int, Map<String,String>>? = try {
            val url = java.net.URL(urlStr)
            val c = (url.openConnection() as java.net.HttpURLConnection).apply {
                requestMethod = "HEAD"
                connectTimeout = timeoutMs
                readTimeout = timeoutMs
                instanceFollowRedirects = false
            }
            val code = c.responseCode
            val headers = c.headerFields.filterKeys { it != null }
                .mapValues { it.value?.joinToString(",") ?: "" }
            c.disconnect()
            code to headers.mapKeys { it.key!!.lowercase() }
        } catch (_: Exception) { null }

        val h1 = head("http://$ip/cgi-bin/luci")
        if (h1 != null) {
            val (code, hdr) = h1
            val loc = hdr["location"]?.lowercase().orEmpty()
            val server = hdr["server"]?.lowercase().orEmpty()
            if (code in listOf(200,401,403)) return@withContext true
            if (code in listOf(301,302) && "/cgi-bin/luci" in loc) return@withContext true
            if ("uhttpd" in server) return@withContext true
        }

        val h2 = head("http://$ip/")
        if (h2 != null) {
            val (code, hdr) = h2
            val server = hdr["server"]?.lowercase().orEmpty()
            if (code in 200..403 && "uhttpd" in server) return@withContext true
        }

        try {
            val url = java.net.URL("https://$ip/cgi-bin/luci")
            val c = (url.openConnection() as javax.net.ssl.HttpsURLConnection).apply {
                requestMethod = "HEAD"
                connectTimeout = timeoutMs
                readTimeout = timeoutMs
            }
            val code = c.responseCode
            val server = (c.getHeaderField("Server") ?: "").lowercase()
            c.disconnect()
            if (code in listOf(200,401,403) || "uhttpd" in server) return@withContext true
        } catch (_: Exception) {}

        try {
            java.net.Socket().use { s ->
                s.connect(java.net.InetSocketAddress(ip, 22), timeoutMs)
                s.soTimeout = timeoutMs
                val buf = ByteArray(128)
                val n = s.getInputStream().read(buf)
                val banner = if (n > 0) String(buf, 0, n).lowercase() else ""
                if ("dropbear" in banner || "openssh" in banner) return@withContext true
            }
        } catch (_: Exception) {}

        false
    }


val BAD_MACS = setOf(
    "02:00:00:00:00:00",
    "00:00:00:00:00:00",
    "ff:ff:ff:ff:ff:ff"
)

fun normalizeMacOrNull(mac: String?): String? {
    val m = mac?.trim()?.lowercase() ?: return null
    return if (m.isBlank() || m in BAD_MACS) null else m
}

suspend fun getPublicIp(): String? = withContext(kotlinx.coroutines.Dispatchers.IO) {
    try {
        val url = java.net.URL("https://api.ipify.org")
        (url.openConnection() as java.net.HttpURLConnection).run {
            connectTimeout = 1500; readTimeout = 1500
            inputStream.bufferedReader().use { it.readText().trim() }.also { disconnect() }
        }
    } catch (_: Exception) { null }
}
