package com.tfg.securerouter.data.app.screens.wifi.model.filter

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class WifiFilterWebRuleState(
    val index: Int = -1,
    val domain: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
fun WifiFilterWebRuleState.toReadableList(): String {
    return domain
}

data class WifiFilterWebsRulesState(
    val rules: List<WifiFilterWebRuleState> = emptyList<WifiFilterWebRuleState>(),
    val nextIndex: Int = -1
)

fun WifiFilterWebsRulesState.toReadableList(): List<String>{
    return rules.map { rule ->
        rule.toString()
    }
}
