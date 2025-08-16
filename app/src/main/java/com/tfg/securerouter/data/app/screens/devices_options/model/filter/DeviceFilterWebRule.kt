package com.tfg.securerouter.data.app.screens.devices_options.model.filter

import android.os.Build
import androidx.annotation.RequiresApi

data class DeviceFilterWebRuleState(
    val index: Int = -1,
    val mac: String = "",
    val domain: String = ""
)

@RequiresApi(Build.VERSION_CODES.O)
fun DeviceFilterWebRuleState.toReadableList(): String {
    return domain
}

data class DeviceFilterWebsRulesState(
    val rules: List<DeviceFilterWebRuleState> = emptyList<DeviceFilterWebRuleState>(),
    val nextIndex: Int = -1
)

fun DeviceFilterWebsRulesState.toReadableList(): List<String>{
    return rules.map { rule ->
        rule.toString()
    }
}
