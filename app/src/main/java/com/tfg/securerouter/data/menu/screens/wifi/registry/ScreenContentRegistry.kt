package com.tfg.securerouter.data.menu.screens.wifi.registry

import com.tfg.securerouter.data.menu.screens.ScreenComponentModelDefault
import com.tfg.securerouter.ui.app.screens.wifi.WifiScreen

/**
 * Registry object that holds all available [WifiScreen]'s contents.
 *
 * This object acts as a centralized list where [WifiScreen]'s content can be added.
 * It avoids the need to modify other parts of the code when extending [WifiScreen] contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render [WifiScreen]'.
 * - New menu options can be registered simply by adding them to the [modules] list.
 *
 * @property modules A list of all registered [WifiScreen]'s contents in the order they should appear.
 *
 * @see ScreenComponentModelDefault for the interface each menu screen must implement.
 * @see WifiScreen for the [WifiScreen]'s class.
 */

class WifiContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
    )
}