package com.tfg.securerouter.ui.app.screens.devices_options.components

import androidx.compose.runtime.Composable
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.devices_options.components.extras.DeviceTimeTable

@Composable
fun DeviceOptionsTImes() {
    ExpandableSection(title = "Tiempos", content = { DeviceTimeTable() })

}