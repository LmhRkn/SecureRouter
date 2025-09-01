package com.tfg.securerouter.data.app.screens.wifi.model.send.vpn

import com.tfg.securerouter.data.router.launchCommand
import com.tfg.securerouter.data.utils.AppSession

object DelVPNPeer {
    fun delVPNPeer(ip: String, name: String) {
        val num = ip.substringBefore('/').substringAfterLast('.')
        val BASE = "/root/vpn"
        val DEV = "$BASE/devices/peer_$num"

        val command = """
            uci -q delete network.$name
            rm -r $DEV
            ifdown vpn 2>/dev/null || true
            ifup vpn
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
