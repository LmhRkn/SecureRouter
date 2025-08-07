package com.tfg.securerouter.data.app.screens.router_selector.registry

import android.content.Context
import android.net.DhcpInfo
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.json.router_selector.RouterSelctorCache
import java.net.InetAddress
import java.net.NetworkInterface
import android.net.wifi.WifiManager
import androidx.compose.ui.platform.LocalContext
import com.tfg.securerouter.ContextProvider
import com.tfg.securerouter.data.router.getRouterIpAddress

fun getRouterList(): List<RouterInfo> {
    val routers: List<RouterInfo> = RouterSelctorCache.getRouters()
    val currentConnectionIdentifier = getCurrentConnectionIdentifier()

    val matchedRouter = routers.find { router ->
        when {
            !router.publicIpOrDomain.isNullOrBlank() -> router.publicIpOrDomain == currentConnectionIdentifier
            !router.localIp.isNullOrBlank() -> router.localIp == currentConnectionIdentifier
            router.mac.isNotBlank() -> router.mac == currentConnectionIdentifier
            else -> false
        }
    }

    return if (matchedRouter != null) {
        listOf(matchedRouter) + routers.filterNot { it == matchedRouter }
    } else {
        routers
    }
}

fun getCurrentConnectionIdentifier(): String? {
    val ip = getRouterIpAddress() // Implementa esta
    val domain = resolveDomainFromIp(ip) // Opcional: si usas no-ip o similar
    val mac = getMacAddress() // Implementa esta

    return domain ?: ip ?: mac
}


fun getMacAddress(): String? {
    try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (intf in interfaces) {
            val mac = intf.hardwareAddress ?: continue
            return mac.joinToString(":") { String.format("%02X", it) }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun resolveDomainFromIp(ip: String?): String? {
    return try {
        if (ip == null) return null
        val inetAddress = InetAddress.getByName(ip)
        val hostName = inetAddress.hostName
        if (hostName != ip) hostName else null
    } catch (e: Exception) {
        null
    }
}
