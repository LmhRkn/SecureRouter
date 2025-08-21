package com.tfg.securerouter.data.app.screens.wifi.model.send.web_filter

import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand

object RemoveFilterWebRuleWifi {
    fun removeFilterWebRuleWifi(filterWebState: WifiFilterWebRuleState) {
        val domain = filterWebState.domain.trim()
        val entry = "/$domain/0.0.0.0"

        val command = """
            changed=0
            if uci -q get dhcp.@dnsmasq[0].address | grep -qxF '$entry'; then
              uci -q del_list dhcp.@dnsmasq[0].address='$entry' && changed=1
            fi
            uci commit dhcp
            service dnsmasq restart
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { true },
            onResult = {}
        )
    }
}
