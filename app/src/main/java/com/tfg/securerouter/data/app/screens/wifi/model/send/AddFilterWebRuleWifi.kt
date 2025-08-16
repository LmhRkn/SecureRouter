package com.tfg.securerouter.data.app.screens.wifi.model.send

import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand


object AddFilterWebRuleWifi {
    fun addFilterWebRuleWifi(wifiFilterWebRule: WifiFilterWebRuleState) {
        val command = """
            uci add firewall rule
            uci set firewall.@rule[-1].name="web_filter_${wifiFilterWebRule.domain}"
            uci set firewall.@rule[-1].src="lan"
            uci set firewall.@rule[-1].dest="wan"
            uci commit firewall
            service firewall reload
        """.trimIndent()
        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
