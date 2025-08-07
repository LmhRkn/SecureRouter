package com.tfg.securerouter.data.router

import android.content.Context
import android.net.DhcpInfo
import android.net.wifi.WifiManager
import com.tfg.securerouter.ContextProvider
import java.net.InetAddress

fun getRouterIpAddress(): String? {
    val context: Context = ContextProvider.get()
    return try {
        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcp: DhcpInfo = wifiManager.dhcpInfo
        val ip = dhcp.gateway
        if (ip == 0) return null
        InetAddress.getByAddress(
            byteArrayOf(
                (ip and 0xFF).toByte(),
                (ip shr 8 and 0xFF).toByte(),
                (ip shr 16 and 0xFF).toByte(),
                (ip shr 24 and 0xFF).toByte()
            )
        ).hostAddress
    } catch (e: Exception) {
        null
    }
}