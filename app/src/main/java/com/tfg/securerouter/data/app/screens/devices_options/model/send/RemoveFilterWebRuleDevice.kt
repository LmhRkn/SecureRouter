package com.tfg.securerouter.data.app.screens.devices_options.model.send

import com.tfg.securerouter.data.router.launchCommand

object RemoveFilterWebRuleDevice {
    fun removeFilterWebRuleDevice(index: Int) {
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
