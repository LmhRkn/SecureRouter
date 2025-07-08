package com.tfg.securerouter.data.screens.device_manager.model.load

import com.tfg.securerouter.data.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.common.screen_components.DeviceModel
import com.tfg.securerouter.data.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.screens.device_manager.state.HistoricalDeviceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HistoricalDeviceModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(HistoricalDeviceState())
    val state: StateFlow<HistoricalDeviceState> = _state

    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "cat /tmp/dhcp.leases",
            cacheKey = "devices_output",
            parse = { parseDevices(it) },
            setState = { _state.value = HistoricalDeviceState(it) }
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
                if (parts.size >= 7) {
                    val statusFlag = parts[parts.size - 2] // Penúltimo parámetro
                    val blockedFlag = parts[parts.size - 1] // Último parámetro

                    // Construir el set de etiquetas
                    val labels = mutableSetOf<DeviceLabel>()

                    // Estado Online/Offline
                    labels += if (statusFlag == "1") DeviceLabel.Online else DeviceLabel.Offline

                    // Etiqueta Blocked
                    if (blockedFlag == "1") {
                        labels += DeviceLabel.Blocked
                    }

                    // Parse and return device info
                    DeviceModel(
                        mac = parts[1],
                        ip = parts[2],
                        hostname = parts[3],
                        labels = labels
                    )
                } else {
                    null // Skip malformed lines
                }
            }
    }
}
