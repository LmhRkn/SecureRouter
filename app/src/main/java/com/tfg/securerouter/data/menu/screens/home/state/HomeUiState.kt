package com.tfg.securerouter.data.menu.screens.home.state

import com.tfg.securerouter.data.menu.screens.home.model.DeviceModel
import com.tfg.securerouter.data.menu.screens.home.model.HomeViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the UI state of the Home screen.
 *
 * This data class holds all the information needed to render the state of the router
 * and its connected devices. It is updated by [HomeViewModel] and observed by the UI
 * using a reactive [StateFlow].
 *
 * @property routerAlias The SSID or alias [String] of the router, if available.
 * @property macAddress The MAC address [String] of the router's wireless interface.
 * @property connectedDevices A list of [DeviceModel] representing all currently connected devices.
 * @property isLoading A [Boolean] flag that indicates whether the data is still being loaded.
 *
 * @see DeviceModel
 * @see HomeViewModel
 */

data class HomeUiState (
    val routerAlias: String?,
    val macAddress: String,
    val connectedDevices: List<DeviceModel> = emptyList(),
    val isLoading: Boolean = true
)
