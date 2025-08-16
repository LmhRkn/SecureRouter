package com.tfg.securerouter.ui.app.screens.devices_options.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.screens.devices_options.model.time.DeviceTimesRulesState
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.devices_options.components.extras.time.DeviceTimeTable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceOptionsTimes(deviceTimesRule: DeviceTimesRulesState, mac: String) {
    ExpandableSection(title = "Tiempos", content = { DeviceTimeTable(deviceTimesRule, mac) })
}