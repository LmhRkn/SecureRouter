package com.tfg.securerouter.data.state

import com.tfg.securerouter.data.model.DeviceModel

data class HomeUiState (
    val routerAlias: String?,
    val routerIp: String,
    val macAddress: String,
    val connectedDevices: List<DeviceModel> = emptyList(),
    val isLoading: Boolean = true
)
