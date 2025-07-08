package com.tfg.securerouter.data.screens.device_manager.state

import com.tfg.securerouter.data.common.screen_components.DeviceModel


data class HistoricalDeviceState (
    val historicalDevices: List<DeviceModel> = emptyList(),
)
