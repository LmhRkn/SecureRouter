package com.tfg.securerouter.data.menu.screens.device_manager.state

import com.tfg.securerouter.data.common.screen_components.DeviceModel


data class HistoricalDeviceState (
    val historicalDevices: List<DeviceModel> = emptyList(),
)
