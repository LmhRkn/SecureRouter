package com.tfg.securerouter.data.menu.screens.administrar.registry

import com.tfg.securerouter.data.menu.screens.ScreenComponentModelDefault
import com.tfg.securerouter.ui.app.screens.administrar.AdministrarDispositivosScreen

/**
 * Registry object that holds all available [AdministrarDispositivosScreen]'s contents.
 *
 * This object acts as a centralized list where [AdministrarDispositivosScreen]'s content can be added.
 * It avoids the need to modify other parts of the code when extending [AdministrarDispositivosScreen] contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render [AdministrarDispositivosScreen]'.
 * - New menu options can be registered simply by adding them to the [modules] list.
 *
 * @property modules A list of all registered [AdministrarDispositivosScreen]'s contents in the order they should appear.
 *
 * @see ScreenComponentModelDefault for the interface each menu screen must implement.
 * @see AdministrarDispositivosScreen for the [AdministrarDispositivosScreen]'s class.
 */

class AdministrarDispositivosScreenContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
    )
}