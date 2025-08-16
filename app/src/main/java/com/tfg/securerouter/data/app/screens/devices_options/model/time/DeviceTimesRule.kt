package com.tfg.securerouter.data.app.screens.devices_options.model.time

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class DeviceTimesRuleState(
    val index: Int = -1,
    val index2: Int? = null,
    val start: String = "",
    val finish: String = "",
    val days: String = "",
    val mac: String = "",
)

@RequiresApi(Build.VERSION_CODES.O)
fun DeviceTimesRuleState.toReadableList(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm[:ss]")
    val startTime = LocalTime.parse(start, formatter)
    val finishTime = LocalTime.parse(finish, formatter)

    val startStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    val finishStr = finishTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    return "Bloquear los $days de $startStr a ${finishStr}h"
}

data class DeviceTimesRulesState(
    val rules: List<DeviceTimesRuleState> = emptyList<DeviceTimesRuleState>(),
    val nextIndex: Int = -1
)

fun DeviceTimesRulesState.toReadableList(): List<String>{
    return rules.map { rule ->
        rule.toString()
    }
}

fun parseDaysToString(days: String): String {
    val mapDias = mapOf(
        "mon" to "L",
        "tue" to "M",
        "wed" to "X",
        "thu" to "J",
        "fri" to "V",
        "sat" to "S",
        "sun" to "D"
    )

    return days.split(",", " ")
        .mapNotNull { dia ->
            mapDias[dia.trim().lowercase()]
        }
        .joinToString(",")
}


fun parseStringToDays(days: String): String {
    val mapDias = mapOf(
        "L" to "mon",
        "M" to "tue",
        "X" to "wed",
        "J" to "thu",
        "V" to "fri",
        "S" to "sat",
        "D" to "sun"
    )

    return days.split(",")
        .mapNotNull { dia ->
            mapDias[dia.trim().uppercase()]
        }
        .joinToString(" ")
}

fun nextDays(days: String): String {
    val weekOrder = listOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")
    val nextMap = weekOrder.zip(weekOrder.drop(1) + weekOrder.first()).toMap()

    return days
        .split(" ", ",", "\t")
        .filter { it.isNotBlank() }.joinToString(" ") { day -> nextMap[day.lowercase()] ?: day }
}
