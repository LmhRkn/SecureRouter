package com.tfg.securerouter.data.app.screens.devices_options.model.send

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRuleState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.nextDays
import com.tfg.securerouter.data.app.common.screen_components.devices.model.parseStringToDays
import com.tfg.securerouter.data.router.launchCommand

object ModifyTimeRuleDevice {
    fun updateTimeRuleDevice(oldRule: DeviceTimesRuleState, newRule: DeviceTimesRuleState) {
        val deleteCmd = buildDeleteCommand(oldRule)
        val addCmd = buildAddCommand(newRule)

        val full = """
            $deleteCmd
            $addCmd
            uci commit firewall
            service firewall restart
        """.trimIndent()

        launchCommand(
            command = full,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }

    private fun buildDeleteCommand(rule: DeviceTimesRuleState): String {
        // Borrar primero el índice MAYOR para no desplazar el menor
        return if (rule.index2 != null) {
            val (a, b) = listOfNotNull(rule.index, rule.index2).sortedDescending()
            """
            uci delete firewall.@rule[$a]
            uci delete firewall.@rule[$b]
            """.trimIndent()
        } else {
            "uci delete firewall.@rule[${rule.index}]"
        }
    }

    private fun buildAddCommand(rule: DeviceTimesRuleState): String {
        val sameDay = """
        IDX=\$(uci add firewall rule)
        uci set firewall.${'$'}IDX.name="time_mac_${rule.mac}_${timeToName(rule.start, rule.finish)}"
        uci set firewall.${'$'}IDX.src="lan"
        uci set firewall.${'$'}IDX.src_mac="${rule.mac}"
        uci set firewall.${'$'}IDX.dest="wan"
        uci set firewall.${'$'}IDX.start_time="${rule.start}"
        uci set firewall.${'$'}IDX.stop_time="${rule.finish}"
        uci set firewall.${'$'}IDX.weekdays="${parseStringToDays(rule.days)}"
        uci set firewall.${'$'}IDX.target="REJECT"
    """.trimIndent()

        val crossMidnight = """
        IDX1=\$(uci add firewall rule)
        uci set firewall.${'$'}IDX1.name="time_mac_${rule.mac}_${timeToName(rule.start, "23:59:59")}"
        uci set firewall.${'$'}IDX1.src="lan"
        uci set firewall.${'$'}IDX1.src_mac="${rule.mac}"
        uci set firewall.${'$'}IDX1.dest="wan"
        uci set firewall.${'$'}IDX1.start_time="${rule.start}"
        uci set firewall.${'$'}IDX1.stop_time="23:59:59"
        uci set firewall.${'$'}IDX1.weekdays="${parseStringToDays(rule.days)}"
        uci set firewall.${'$'}IDX1.target="REJECT"

        IDX2=\$(uci add firewall rule)
        uci set firewall.${'$'}IDX2.name="time_mac_${rule.mac}_${timeToName("00:00:00", rule.finish)}"
        uci set firewall.${'$'}IDX2.src="lan"
        uci set firewall.${'$'}IDX2.src_mac="${rule.mac}"
        uci set firewall.${'$'}IDX2.dest="wan"
        uci set firewall.${'$'}IDX2.start_time="00:00:00"
        uci set firewall.${'$'}IDX2.stop_time="${rule.finish}"
        uci set firewall.${'$'}IDX2.weekdays="${nextDays(parseStringToDays(rule.days))}"
        uci set firewall.${'$'}IDX2.target="REJECT"
    """.trimIndent()

        return if (rule.index2 != null) crossMidnight else sameDay
    }

    private fun timeToName(start: String, finish: String): String {
        return "${start.dropLast(3).split(':').joinToString("-")}_${finish.dropLast(3).split(':').joinToString("-")}"
    }
}
