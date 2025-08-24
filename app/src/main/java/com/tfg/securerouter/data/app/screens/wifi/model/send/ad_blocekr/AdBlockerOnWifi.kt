package com.tfg.securerouter.data.app.screens.wifi.model.send.ad_blocekr

import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand


object AdBlockerOnWifi {
    fun adBlockerOnWifi() {
        val command = """
            uci set dhcp.@dnsmasq[0].port="54"
            uci commit dhcp
            service dnsmasq restart
            service odhcpd restart

            service adguardhome enable
            service adguardhome start
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { true },
            onResult = {}
        )
    }
}
