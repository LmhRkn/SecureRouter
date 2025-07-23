package com.tfg.securerouter.data.app.screens.home.state.load

import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceModel
import com.tfg.securerouter.ui.app.screens.home.HomeScreen
import com.tfg.securerouter.ui.app.screens.home.components.ConnectedDevicesList
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the UI state of the component [ConnectedDevicesList] of the [HomeScreen].
 *
 * This data class holds all the information needed to render the state of the router
 * and its connected devices. It is updated by [HomeRouterInfoModel] and observed by the UI
 * using a reactive [StateFlow].
 *
 * @property connectedDevices A list of [DeviceModel] representing all currently connected devices.
 *
 * @see HomeScreen
 * @see ConnectedDevicesList
 * @see DeviceModel
 */

data class ConnectedDeviceState (
    val connectedDevices: List<DeviceModel> = emptyList(),
)
