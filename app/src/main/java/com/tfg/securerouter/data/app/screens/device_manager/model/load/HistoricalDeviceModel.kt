package com.tfg.securerouter.data.app.screens.device_manager.model.load

import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.common.devices.DevicesListModel
import com.tfg.securerouter.data.app.screens.defaults.ScreenComponentModelDevicesDefault
import com.tfg.securerouter.data.app.screens.device_manager.state.HistoricalDeviceState
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import kotlinx.coroutines.flow.MutableStateFlow
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
    createState = { HistoricalDeviceState(it) }
) {

    private val _state = MutableStateFlow(HistoricalDeviceState())
    override val state: StateFlow<HistoricalDeviceState> = _state

    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "cat /tmp/dhcp.leases",
            cacheKey = "devices_output",
            parse = { parseDevices(it) },
            setState = { _state.value = HistoricalDeviceState(it) }
        )
    }
}

