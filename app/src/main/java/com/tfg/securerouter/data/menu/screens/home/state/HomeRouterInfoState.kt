package com.tfg.securerouter.data.menu.screens.home.state

import HomeScreen
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.data.menu.screens.home.model.load.HomeRouterInfoModel
import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the UI state of the component [HomeRouterInfoSection] of [HomeScreen].
 *
 * This data class holds all the information needed to render the state of the router
 * and its connected devices. It is updated by [HomeRouterInfoModel] and observed by the UI
 * using a reactive [StateFlow].
 *
 * @property routerAlias The SSID or alias [String] of the router, if available.
 * @property macAddress The MAC address [String] of the router's wireless interface.
 *
 * @see HomeScreen
 * @see HomeRouterInfoSection
 */

data class HomeRouterInfoState (
    val routerAlias: String? = null,
    val macAddress: String = ""
)
