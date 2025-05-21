package com.tfg.securerouter.data.main_screen.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.tfg.securerouter.data.main_screen.state.TopBarModel

/**
 * ViewModel responsible for managing the state of the top app bar.
 *
 * Responsibilities:
 * - Holds and updates the [TopBarModel] which includes:
 *     - The current title shown in the top bar.
 *     - Router connection status.
 *     - VPN connection status.
 * - Exposes the state as an immutable [State] object for UI composables to observe.
 *
 * Usage:
 * This ViewModel is typically shared with composables that render the top bar UI.
 *
 * @property topBarState Publicly exposed read-only state of the top bar for UI observation.
 *
 * Functions:
 * - updateTitle(newTitle): Updates the title shown in the top bar.
 * - updateRouterConnected(connected): Updates the router connection status.
 * - updateVpnConnected(connected): Updates the VPN connection status.
 *
 * @see TopBarModel
 */
class TopBarViewModel : ViewModel() {

    // Internal mutable state holding the top bar's UI data
    private val _topBarState = mutableStateOf(TopBarModel())

    // Public immutable state observed by the UI
    val topBarState: State<TopBarModel> = _topBarState

    /**
     * Updates the title of the top bar.
     * @param newTitle The new title string to display.
     */
    fun updateTitle(newTitle: String) {
        _topBarState.value = _topBarState.value.copy(title = newTitle)
    }

    /**
     * Updates the router connection status.
     * @param connected True if the router is connected, false otherwise.
     */
    fun updateRouterConnected(connected: Boolean) {
        _topBarState.value = _topBarState.value.copy(routerConnected = connected)
    }

    /**
     * Updates the VPN connection status.
     * @param connected True if the VPN is connected, false otherwise.
     */
    fun updateVpnConnected(connected: Boolean) {
        _topBarState.value = _topBarState.value.copy(vpnConnected = connected)
    }
}

