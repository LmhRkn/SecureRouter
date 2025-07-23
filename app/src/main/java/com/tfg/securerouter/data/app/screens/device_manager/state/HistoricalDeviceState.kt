package com.tfg.securerouter.data.app.screens.device_manager.state

import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceModel

/**
 * Represents the UI state for the historical devices screen.
 *
 * This data class holds the immutable state exposed to the UI layer,
 * containing the list of devices retrieved from the router's history.
 *
 * @property historicalDevices The list of historical [DeviceModel] instances.
 * Defaults to an empty list when no data is loaded.
 */

data class HistoricalDeviceState (
    val historicalDevices: List<DeviceModel> = emptyList(),
)
