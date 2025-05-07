package com.tfg.securerouter.ui.router.windows.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.tfg.securerouter.ui.router.windows.home.model.DeviceModel
import com.tfg.securerouter.ui.router.windows.home.state.HomeUiState

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            routerAlias = null,
            routerName = "OpenWRT",
            macAddress = "00:11:22:33:44:55",
            isRouterConnected = true,
            isVpnActive = false,
            connectedDevices = listOf(
                DeviceModel("Laptop", "192.168.1.2"),
                DeviceModel(null, "192.168.1.3")
            )
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState
}
