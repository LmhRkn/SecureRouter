package com.tfg.securerouter.data.app.screens.device_manager.model.load

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.common.devices.DevicesListModel
import com.tfg.securerouter.data.app.screens.defaults.ScreenComponentModelDevicesDefault
import com.tfg.securerouter.data.app.screens.device_manager.state.HistoricalDeviceState
import kotlinx.coroutines.flow.StateFlow

/**
 * View model responsible for loading and managing the historical list of connected devices.
 *
 * Usage:
 * This model retrieves device data from the router (via the `dhcp.leases` file), parses it into
 * a list of [DeviceModel], and exposes it through a [StateFlow] for reactive UI updates.
 *
 * It extends [ScreenComponentModelDevicesDefault] to integrate with screen lifecycle management.
 *
 * @property sharedCache A shared in-memory cache used to store raw command outputs.
 * @property state A [StateFlow] exposing the current [HistoricalDeviceState] for UI consumption.
 *
 * @see HistoricalDeviceState
 * @see DeviceModel
 * @see safeLoad
 */

class HistoricalDeviceModel(
    sharedCache: MutableMap<String, Any>
) : DevicesListModel<HistoricalDeviceState>(
    sharedCache = sharedCache,
    createState = { list -> HistoricalDeviceState(list) }
)

