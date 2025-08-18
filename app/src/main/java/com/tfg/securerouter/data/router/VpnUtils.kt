package com.tfg.securerouter.data.router

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.tfg.securerouter.ContextProvider
import com.jcraft.jsch.SocketFactory
import java.net.InetSocketAddress
import java.net.Socket

object VpnUtils {
    /** Devuelve la Network activa si es VPN, o null si no hay VPN o no es la activa. */
    fun getActiveVpnNetwork(): Network? {
        val ctx = ContextProvider.get()
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val active = cm.activeNetwork ?: return null
        val caps = cm.getNetworkCapabilities(active) ?: return null
        return if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) active else null
    }

    /** También puedes buscar cualquier network que sea VPN (por si la activa no lo es): */
    fun findAnyVpnNetwork(): Network? {
        val ctx = ContextProvider.get()
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.allNetworks.firstOrNull { n ->
            cm.getNetworkCapabilities(n)?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
        }
    }
}

/** SocketFactory de JSch que usa el SocketFactory de una Network de Android (VPN). */
class AndroidNetworkSocketFactory(private val network: Network) : SocketFactory {
    override fun createSocket(host: String, port: Int): Socket {
        val sf = network.socketFactory
        val socket = sf.createSocket()
        socket.connect(InetSocketAddress(host, port), 10_000)
        return socket
    }
    override fun getInputStream(socket: Socket) = socket.getInputStream()
    override fun getOutputStream(socket: Socket) = socket.getOutputStream()
}
