package com.tfg.securerouter.ui.app.screens.devices_options.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebsRulesState
import com.tfg.securerouter.data.app.screens.devices_options.model.time.DeviceTimesRulesState
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.devices_options.components.extras.filter.DeviceFilterWebTable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceOptionsFilters(deviceFilterWebRule: DeviceFilterWebsRulesState, mac: String) {
    ExpandableSection(title = "Filtros", content = { DeviceFilterWebTable(deviceFilterWebRule, mac) })
}