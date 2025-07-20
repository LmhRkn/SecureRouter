package com.tfg.securerouter.data.menu.screens.filter.registry

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.ui.app.screens.settings.SettingsScreen

/**
 * Registry object that holds all available [SettingsScreen]'s contents.
 *
 * This object acts as a centralized list where [SettingsScreen]'s content can be added.
 * It avoids the need to modify other parts of the code when extending [SettingsScreen] contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render [SettingsScreen]'.
 * - New menu options can be registered simply by adding them to the [modules] list.
 *
 * @property modules A list of all registered [SettingsScreen]'s contents in the order they should appear.
 *
 * @see ScreenComponentModelDefault for the interface each menu screen must implement.
 * @see SettingsScreen for the [SettingsScreen]'s class.
 */

class SettingsScreenContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
    )
}