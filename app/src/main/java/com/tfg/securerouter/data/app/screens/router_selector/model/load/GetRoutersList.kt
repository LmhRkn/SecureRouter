package com.tfg.securerouter.data.app.screens.router_selector.model.load

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.RouterLabel
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.router.getCurrentBssid
import com.tfg.securerouter.data.router.getCurrentConnectionIdentifiers
import com.tfg.securerouter.data.router.getGatewayMacAddress
import com.tfg.securerouter.data.router.getRouterIpAddress
import com.tfg.securerouter.data.router.getPublicIp
import com.tfg.securerouter.data.router.isOpenWrtBySsh
import com.tfg.securerouter.data.router.isOpenWrtNoAuth
import com.tfg.securerouter.data.router.normalizeMacOrNull
import com.tfg.securerouter.data.router.resolveDomainIps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "RouterDetect"

fun getRouterList(ephemeral: RouterInfo? = null): List<RouterInfo> {
    val routers: List<RouterInfo> = RouterSelectorCache.getRouters()

    val ids: Set<String> = getCurrentConnectionIdentifiers()
        .map { it.trim().lowercase() }
        .toSet()
    Log.d(TAG, "IDs actuales (MAC/BSSID/IP): $ids")

    data class Annot(val router: RouterInfo, val isCurrent: Boolean)

    fun isMatch(router: RouterInfo): Boolean {
        val candidates = sequenceOf(
            normalizeMacOrNull(router.mac),
            router.localIp?.trim()?.lowercase(),
            router.publicIpOrDomain?.trim()?.lowercase()
        ).filterNot { it.isNullOrBlank() }.toSet()

        val match = ids.isNotEmpty() && candidates.any { it in ids }
        Log.d(TAG, "Router ${router.id}/${router.name} -> cands=$candidates match=$match")
        return match
    }

    val annotated: List<Annot> = routers.map { r ->
        val m = isMatch(r)
        val newLabels =
            if (m) r.labels + RouterLabel.Online - RouterLabel.Offline
            else   r.labels - RouterLabel.Online + RouterLabel.Offline
        Annot(r.copy(labels = newLabels), m)
    }

    val hasMatch = annotated.any { it.isCurrent }
    val orderedSaved = annotated.sortedByDescending { it.isCurrent }.map { it.router }

    return if (!hasMatch && ephemeral != null) listOf(ephemeral) + orderedSaved else orderedSaved
}

suspend fun detectEphemeralOpenWrt(): RouterInfo? = withContext(Dispatchers.IO) {
    val ip = getRouterIpAddress() ?: return@withContext null
    val mac = getGatewayMacAddress(ip) ?: getCurrentBssid()
    val macNorm = normalizeMacOrNull(mac)

    val ok = isOpenWrtNoAuth(
        ip = ip,
        viaVpn = false,
    )
    if (!ok) return@withContext null

    return@withContext RouterInfo(
        name = "Router detectado",
        mac = macNorm?.uppercase() ?: "UNKNOWN",
        localIp = ip,
        publicIpOrDomain = null,
        isVpn = false,
        id = -1,
        installerPackage = null,
        labels = setOf(RouterLabel.New, RouterLabel.Online),
        vpnHost = null,
        vpnPort = 22,
        sshHostKeyFingerprint = null
    )
}

suspend fun detectEphemeralOpenWrtViaVpn(hostInTunnel: String): RouterInfo? =
    withContext(Dispatchers.IO) {
        if (!isOpenWrtBySsh(hostInTunnel, viaVpn = true)) return@withContext null
        RouterInfo(
            name = "Router detectado por VPN",
            mac = "UNKNOWN",
            localIp = null,
            publicIpOrDomain = null,
            isVpn = true,
            id = -1,
            labels = setOf(RouterLabel.New, RouterLabel.Online)
        )
    }

suspend fun findMatchingNoIpRouter(routers: List<RouterInfo>): RouterInfo? {
    val current = getPublicIp()?.trim()?.lowercase() ?: return null
    Log.d("findMatchingNoIpRouter", "router: $routers")
    Log.d("findMatchingNoIpRouter", "current: $current")
    for (r in routers) {
        Log.d("findMatchingNoIpRouter", "r: $r")
        val target = r.publicIpOrDomain?.trim()?.lowercase().orEmpty()
        Log.d("findMatchingNoIpRouter", "target: $target")
        if (target.isBlank()) continue

        val ips: Set<String> = when {
            Regex("""^\d{1,3}(\.\d{1,3}){3}$""").matches(target) -> setOf(target)
            target.any { it.isLetter() } -> resolveDomainIps(target)
            else -> emptySet()
        }

        if (current in ips) return r
    }
    return null
}
