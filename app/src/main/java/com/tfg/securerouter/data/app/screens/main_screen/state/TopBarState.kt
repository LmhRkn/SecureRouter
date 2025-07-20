package com.tfg.securerouter.data.app.screens.main_screen.state

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class that holds the UI state for the application's top app bar.
 *
 * Usage:
 * - Used by the TopBar composable and its ViewModel to display dynamic content.
 * - Encapsulates the current screen title and connection status indicators.
 *
 * @property title A [String] representing the text to display in the center of the top bar.
 * @property routerConnected A [Boolean] indicating whether the router is currently connected.
 * @property vpnConnected A [Boolean] indicating whether the VPN is currently connected.
 */
data class TopBarModel(
    val title: String = "Home",
    val routerConnected: Boolean = true,
    val vpnConnected: Boolean = false
)