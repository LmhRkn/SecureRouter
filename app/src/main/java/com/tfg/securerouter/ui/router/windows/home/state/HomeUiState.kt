package com.tfg.securerouter.ui.router.windows.home.state

import com.tfg.securerouter.ui.router.windows.home.model.DeviceModel

data class HomeUiState(
    val routerAlias: String?,
    val routerName: String,
    val macAddress: String,
    val isRouterConnected: Boolean,
    val isVpnActive: Boolean,
    val connectedDevices: List<DeviceModel>
)