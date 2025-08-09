package com.tfg.securerouter.data.app.screens.devices_options.model.send

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRuleState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.nextDays
import com.tfg.securerouter.data.app.common.screen_components.devices.model.parseStringToDays
import com.tfg.securerouter.data.router.launchCommand


object AddTimeRuleDevice {
    fun addTimeRuleDevice(deviceTimesRule: DeviceTimesRuleState) {
        val commandSameDay = """
            uci add firewall rule
            uci set firewall.@rule[-1].name="time_mac_${deviceTimesRule.mac}_${timeToName(deviceTimesRule.start, deviceTimesRule.finish)}"
            uci set firewall.@rule[-1].src="lan"
            uci set firewall.@rule[-1].src_mac="${deviceTimesRule.mac}"
            uci set firewall.@rule[-1].dest="wan"
            uci set firewall.@rule[-1].start_time="${deviceTimesRule.start}"
            uci set firewall.@rule[-1].stop_time="${deviceTimesRule.finish}"
            uci set firewall.@rule[-1].weekdays="${parseStringToDays(deviceTimesRule.days)}"
            uci set firewall.@rule[-1].target="REJECT"
            
            uci commit firewall
            service firewall restart
        """.trimIndent()

        val commandDifferentDays = """
            uci add firewall rule
            uci set firewall.@rule[-1].name="time_mac_${deviceTimesRule.mac}_${timeToName(deviceTimesRule.start, "23:59:59")}"
            uci set firewall.@rule[-1].src="lan"
            uci set firewall.@rule[-1].src_mac="${deviceTimesRule.mac}"
            uci set firewall.@rule[-1].dest="wan"
            uci set firewall.@rule[-1].start_time="${deviceTimesRule.start}"
            uci set firewall.@rule[-1].stop_time="23:59:59"
            uci set firewall.@rule[-1].weekdays="${parseStringToDays(deviceTimesRule.days)}"
            uci set firewall.@rule[-1].target="REJECT"
            
            uci add firewall rule
            uci set firewall.@rule[-1].name="time_mac_${deviceTimesRule.mac}_${timeToName("00:00:00", deviceTimesRule.finish)}"
            uci set firewall.@rule[-1].src="lan"
            uci set firewall.@rule[-1].src_mac="${deviceTimesRule.mac}"
            uci set firewall.@rule[-1].dest="wan"
            uci set firewall.@rule[-1].start_time="00:00:00"
            uci set firewall.@rule[-1].stop_time="${deviceTimesRule.finish}"
            uci set firewall.@rule[-1].weekdays="${nextDays(parseStringToDays(deviceTimesRule.days))}"
            uci set firewall.@rule[-1].target="REJECT"
            
            uci commit firewall
            service firewall restart
        """.trimIndent()

        val command = if (deviceTimesRule.index2 != null) commandDifferentDays else commandSameDay

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }

    private fun timeToName(start: String, finish: String): String {
        return "${start.dropLast(3).split(":").joinToString("-")}_${finish.dropLast(3).split(":").joinToString("-")}"
    }
}
