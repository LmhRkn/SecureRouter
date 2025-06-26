package com.tfg.securerouter.data.menu.screens.home.registry

import com.tfg.securerouter.data.menu.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.menu.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.menu.screens.home.model.load.HomeRouterInfoModel

/**
 * Registry object that holds all available [HomeScreen]'s contents.
 *
 * This object acts as a centralized list where [HomeScreen]'s content can be added.
 * It avoids the need to modify other parts of the code when extending [HomeScreen] contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render [HomeScreen]'.
 * - New menu options can be registered simply by adding them to the [modules] list.
 *
 * @property modules A list of all registered [HomeScreen]'s contents in the order they should appear.
 *
 * @see ScreenComponentModelDefault for the interface each menu screen must implement.
 * @see HomeScreen for the [HomeScreen]'s class.
 */

class HomeScreenContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
        HomeRouterInfoModel(sharedCache),
        ConnectedDeviceModel(sharedCache)
    )
}