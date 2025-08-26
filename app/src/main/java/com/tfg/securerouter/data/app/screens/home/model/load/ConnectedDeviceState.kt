package com.tfg.securerouter.data.app.screens.home.model.load

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel

/**
 * Represents the UI state of the component [com.tfg.securerouter.ui.app.screens.home.components.ConnectedDevicesList] of the [com.tfg.securerouter.ui.app.screens.home.HomeScreen].
 *
 * This data class holds all the information needed to render the state of the router
 * and its connected devices. It is updated by [HomeRouterInfoModel] and observed by the UI
 * using a reactive [kotlinx.coroutines.flow.StateFlow].
 *
 * @property connectedDevices A list of [com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel] representing all currently connected devices.
 *
 * @see com.tfg.securerouter.ui.app.screens.home.HomeScreen
 * @see com.tfg.securerouter.ui.app.screens.home.components.ConnectedDevicesList
 * @see com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
 */

data class ConnectedDeviceState (
    val connectedDevices: List<DeviceModel> = emptyList(),
)