package com.tfg.securerouter.data.menu.screens.home.state

import com.tfg.securerouter.data.menu.screens.home.model.load.DeviceModel
import com.tfg.securerouter.ui.common.home_screen.ConnectedDevicesList
import kotlinx.coroutines.flow.StateFlow
import com.tfg.securerouter.data.menu.screens.home.model.load.HomeRouterInfoModel

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
