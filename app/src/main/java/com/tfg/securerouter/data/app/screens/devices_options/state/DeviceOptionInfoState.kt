package com.tfg.securerouter.data.app.screens.devices_options.state

import androidx.compose.ui.graphics.vector.ImageVector

data class DeviceOptionInfoState (
    val deviceAlias: String? = null,
    val macAddress: String = "",
    val ip: String = "",
    val icon: ImageVector,
    val iconDescription: String
)
