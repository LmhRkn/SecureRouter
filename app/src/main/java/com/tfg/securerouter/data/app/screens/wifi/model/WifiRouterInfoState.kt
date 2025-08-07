package com.tfg.securerouter.data.app.screens.wifi.model

import com.tfg.securerouter.data.app.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import kotlinx.coroutines.flow.StateFlow
import com.tfg.securerouter.ui.app.screens.home.HomeScreen

data class WifiRouterInfoState (
    val routerAlias: String? = null,
    val macAddress: String = "",
    val wirelessName: String = "",
    val password: String = "",
)
