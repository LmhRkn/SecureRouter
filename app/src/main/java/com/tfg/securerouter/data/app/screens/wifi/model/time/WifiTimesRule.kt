package com.tfg.securerouter.data.app.screens.wifi.model.time

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class WifiTimesRuleState(
    val index: Int = -1,
    val index2: Int? = null,
    val start: String = "",
    val finish: String = "",
    val days: String = "",
)

@RequiresApi(Build.VERSION_CODES.O)
fun WifiTimesRuleState.toReadableList(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm[:ss]")
    val startTime = LocalTime.parse(start, formatter)
    val finishTime = LocalTime.parse(finish, formatter)

    val startStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    val finishStr = finishTime.format(DateTimeFormatter.ofPattern("HH:mm"))

    return "Bloquear los $days de $startStr a ${finishStr}h"
}

data class WifiTimesRulesState(
    val rules: List<WifiTimesRuleState> = emptyList<WifiTimesRuleState>(),
    val nextIndex: Int = -1
)

fun WifiTimesRulesState.toReadableList(): List<String>{
    return rules.map { rule ->
        rule.toString()
    }
}
