package com.tfg.securerouter.data.app.screens.home.model.load

/**
 * Represents the UI state of the component [com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection] of [com.tfg.securerouter.ui.app.screens.home.HomeScreen].
 *
 * This data class holds all the information needed to render the state of the router
 * and its connected devices. It is updated by [HomeRouterInfoModel] and observed by the UI
 * using a reactive [kotlinx.coroutines.flow.StateFlow].
 *
 * @property routerAlias The SSID or alias [String] of the router, if available.
 * @property macAddress The MAC address [String] of the router's wireless interface.
 *
 * @see com.tfg.securerouter.ui.app.screens.home.HomeScreen
 * @see com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
 */

data class HomeRouterInfoState (
    val routerAlias: String? = null,
    val macAddress: String = "",
    val wirelessName: String = "a",
)