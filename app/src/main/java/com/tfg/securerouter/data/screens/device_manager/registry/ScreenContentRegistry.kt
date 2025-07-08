package com.tfg.securerouter.data.screens.device_manager.registry

import com.tfg.securerouter.data.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.screens.device_manager.model.load.HistoricalDeviceModel
import com.tfg.securerouter.ui.app.screens.device_manager.DeviceManagerScreen

/**
 * Registry object that holds all available [DeviceManagerScreen]'s contents.
 *
 * This object acts as a centralized list where [DeviceManagerScreen]'s content can be added.
 * It avoids the need to modify other parts of the code when extending [DeviceManagerScreen] contents,
 * promoting scalability and modularity.
 *
 * Usage:
 * - Used by the UI to render [DeviceManagerScreen]'.
 * - New menu options can be registered simply by adding them to the [modules] list.
 *
 * @property modules A list of all registered [DeviceManagerScreen]'s contents in the order they should appear.
 *
 * @see ScreenComponentModelDefault for the interface each menu screen must implement.
 * @see DeviceManagerScreen for the [DeviceManagerScreen]'s class.
 */

class AdministrarDispositivosScreenContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
        HistoricalDeviceModel(sharedCache)
    )
}