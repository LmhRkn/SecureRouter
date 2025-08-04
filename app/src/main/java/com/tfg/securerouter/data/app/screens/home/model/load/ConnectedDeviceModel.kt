package com.tfg.securerouter.data.app.screens.home.model.load

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.common.devices.DevicesListModel
import com.tfg.securerouter.data.app.screens.defaults.ScreenComponentModelDevicesDefault
import com.tfg.securerouter.data.app.screens.home.state.load.ConnectedDeviceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Model class responsible for loading and managing the state of connected devices.
 *
 * This implementation of [ScreenComponentModelDevicesDefault] fetches the list of currently
 * connected devices from the router’s DHCP leases file, parses them into [DeviceModel] instances,
 * and exposes the resulting state via a [StateFlow].
 *
 * @property sharedCache In-memory cache shared with other modules to reuse raw command outputs.
 * @property state A [StateFlow] exposing the current [ConnectedDeviceState].
 *
 * @see ScreenComponentModelDevicesDefault
 * @see ConnectedDeviceState
 */
class ConnectedDeviceModel(
    override val sharedCache: MutableMap<String, Any>,
) : DevicesListModel<ConnectedDeviceState>(
    sharedCache = sharedCache,
    createState = { ConnectedDeviceState(it) }
) {
    private val _state = MutableStateFlow(ConnectedDeviceState())
    override val state: StateFlow<ConnectedDeviceState> = _state

    /**
     * Loads the list of connected devices from the router and updates the state.
     *
     * This method:
     * - Executes the command `cat /tmp/dhcp.leases` to fetch raw device data.
     * - Parses the output into a list of [DeviceModel]s.
     * - Updates [_state] with the resulting [ConnectedDeviceState].
     *
     * @return `true` if the data was successfully loaded; `false` otherwise.
     */
    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "cat /tmp/dhcp.leases",
            cacheKey = "devices_output",
            parse = { parseDevices(it) },
            setState = { _state.value = ConnectedDeviceState(it) }
        )
    }
}
