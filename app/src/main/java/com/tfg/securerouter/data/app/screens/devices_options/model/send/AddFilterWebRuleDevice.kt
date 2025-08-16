package com.tfg.securerouter.data.app.screens.devices_options.model.send

import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebRuleState
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand


object AddFilterWebRuleDevice {
    fun addFilterWebRuleDevice(deviceFilterWebRule: DeviceFilterWebRuleState) {
        val command = """
            uci add firewall rule
            uci set firewall.@rule[-1].name="web_filter_${deviceFilterWebRule.mac}_${deviceFilterWebRule.domain}"
            uci set firewall.@rule[-1].src_mac="${deviceFilterWebRule.mac}"
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
