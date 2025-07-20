package com.tfg.securerouter.data.app.screens.filter.registry

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.ui.app.screens.filter.FilterScreen

/**
 * Registry object that holds all available [FilterScreen]'s contents.
 *
 * This object acts as a centralized list where [FilterScreen]'s content can be added.
 * It avoids the need to modify other parts of the code when extending [FilterScreen] contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render [FilterScreen]'.
 * - New menu options can be registered simply by adding them to the [modules] list.
 *
 * @property modules A list of all registered [FilterScreen]'s contents in the order they should appear.
 *
 * @see ScreenComponentModelDefault for the interface each menu screen must implement.
 * @see FilterScreen for the [FilterScreen]'s class.
 */

class FilterScreenContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
    )
}