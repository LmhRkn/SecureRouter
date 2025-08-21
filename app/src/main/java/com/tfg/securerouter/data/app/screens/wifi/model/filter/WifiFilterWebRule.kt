package com.tfg.securerouter.data.app.screens.wifi.model.filter

import android.os.Build
import androidx.annotation.RequiresApi

data class WifiFilterWebRuleState(
    val index: Int = -1,
    val domain: String = "",
    val mac: String? = null,
    val source: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
fun WifiFilterWebRuleState.toReadableList(): String {
    return if (mac == null) domain else domain
}

data class WifiFilterWebsRulesState(
    val rules: List<WifiFilterWebRuleState> = emptyList(),
    val nextIndex: Int = -1
)

fun WifiFilterWebsRulesState.toReadableList(): List<String> {
    return rules.map { if (it.mac == null) it.domain else it.domain}
}
