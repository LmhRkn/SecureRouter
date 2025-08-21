package com.tfg.securerouter.data.app.screens.wifi.model.send.web_filter

import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand


object AddFilterWebRuleWifi {
    fun addFilterWebRuleWifi(filterWebState: WifiFilterWebRuleState) {
        val domain = filterWebState.domain.trim()
        val entry = "/$domain/0.0.0.0"

        val command = """
            if ! uci -q get dhcp.@dnsmasq[0].address | grep -qxF '$entry'; then
              uci add_list dhcp.@dnsmasq[0].address='$entry'
              uci commit dhcp
              service dnsmasq restart
            fi
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { true },
            onResult = {}
        )
    }
}
