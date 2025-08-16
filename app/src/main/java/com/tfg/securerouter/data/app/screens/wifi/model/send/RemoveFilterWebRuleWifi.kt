package com.tfg.securerouter.data.app.screens.wifi.model.send

import com.tfg.securerouter.data.router.launchCommand

object RemoveFilterWebRuleWifi {
    fun removeFilterWebRuleWifi(index: Int) {
        val command = """
            uci delete firewall.@rule[$index]
            uci commit firewall
            service firewall restart
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
