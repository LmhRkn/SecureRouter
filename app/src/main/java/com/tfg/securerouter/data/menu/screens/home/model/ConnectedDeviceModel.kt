package com.tfg.securerouter.data.menu.screens.home.model

import com.tfg.securerouter.data.menu.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.menu.screens.home.state.ConnectedDeviceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ConnectedDeviceModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(ConnectedDeviceState())
    val state: StateFlow<ConnectedDeviceState> = _state

    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "cat /tmp/dhcp.leases",
            cacheKey = "devices_output",
            parse = { parseDevices(it) },
            setState = { _state.value = ConnectedDeviceState(it) }
        )
    }


    /**
     * Parses the DHCP leases output to identify connected devices.
     *
     * @param output The raw string output from `cat /tmp/dhcp.leases`.
     * @return A list of [DeviceModel], each representing a connected device with:
     *   - MAC address
     *   - IP address
     *   - Hostname
     */
    private fun parseDevices(output: String): List<DeviceModel> {
        return output.lines()
            .filter { it.isNotBlank() } // Remove empty lines
            .mapNotNull { line ->
                val parts = line.split(" ")
                if (parts.size >= 4) {
                    // Parse and return device info
                    DeviceModel(
                        mac = parts[1],
                        ip = parts[2],
                        hostname = parts[3]
                    )
                } else {
                    null // Skip malformed lines
                }
            }
    }
}
