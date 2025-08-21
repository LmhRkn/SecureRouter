package com.tfg.securerouter.data.app.screens.wifi.model.send.time

import com.tfg.securerouter.data.router.launchCommand

object RemoveTimeRuleWifi {
    fun removeTimeRuleWifi(index: Int, index2: Int?) {
        val commandSameDay = """
            uci delete firewall.@rule[$index]
            uci commit firewall
            service firewall restart
        """.trimIndent()

        val commandDifferentDays = """
            uci delete firewall.@rule[$index2]
            uci commit firewall
            uci delete firewall.@rule[$index]
            uci commit firewall
            service firewall restart
        """.trimIndent()

        val command = if (index2 != null) commandDifferentDays else commandSameDay

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
