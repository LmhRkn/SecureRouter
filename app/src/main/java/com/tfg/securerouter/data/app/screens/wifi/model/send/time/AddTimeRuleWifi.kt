package com.tfg.securerouter.data.app.screens.wifi.model.send.time

import com.tfg.securerouter.data.app.screens.devices_options.model.time.nextDays
import com.tfg.securerouter.data.app.screens.devices_options.model.time.parseStringToDays
import com.tfg.securerouter.data.app.screens.wifi.model.time.WifiTimesRuleState
import com.tfg.securerouter.data.router.launchCommand


object AddTimeRuleWifi {
    fun addTimeRuleWifi(wifiTimesRule: WifiTimesRuleState) {
        val commandSameDay = """
            uci add firewall rule
            uci set firewall.@rule[-1].name="time_wifi_${timeToName(wifiTimesRule.start, wifiTimesRule.finish)}"
            uci set firewall.@rule[-1].src="lan"
            uci set firewall.@rule[-1].dest="wan"
            uci set firewall.@rule[-1].start_time="${wifiTimesRule.start}"
            uci set firewall.@rule[-1].stop_time="${wifiTimesRule.finish}"
            uci set firewall.@rule[-1].weekdays="${parseStringToDays(wifiTimesRule.days)}"
            uci set firewall.@rule[-1].target="REJECT"
            uci commit firewall
            service firewall reload
        """.trimIndent()

        val commandDifferentDays = """
            uci add firewall rule
            uci add firewall rule
        
            uci set firewall.@rule[-2].name="time_wifi_${timeToName(wifiTimesRule.start, "23:59:59")}"
            uci set firewall.@rule[-2].src="lan"
            uci set firewall.@rule[-2].dest="wan"
            uci set firewall.@rule[-2].start_time="${wifiTimesRule.start}"
            uci set firewall.@rule[-2].stop_time="23:59:59"
            uci set firewall.@rule[-2].weekdays="${parseStringToDays(wifiTimesRule.days)}"
            uci set firewall.@rule[-1].target="REJECT"
        
            uci set firewall.@rule[-1].name="time_wifi_${timeToName("00:00:00", wifiTimesRule.finish)}"
            uci set firewall.@rule[-1].src="lan"
            uci set firewall.@rule[-1].dest="wan"
            uci set firewall.@rule[-1].start_time="00:00:00"
            uci set firewall.@rule[-1].stop_time="${wifiTimesRule.finish}"
            uci set firewall.@rule[-1].weekdays="${nextDays(parseStringToDays(wifiTimesRule.days))}"
            uci set firewall.@rule[-1].target="REJECT"
            
            uci commit firewall
            service firewall restart
        """.trimIndent()

        val command = if (wifiTimesRule.index2 != null) commandDifferentDays else commandSameDay

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
