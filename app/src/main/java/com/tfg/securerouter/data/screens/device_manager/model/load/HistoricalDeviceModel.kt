package com.tfg.securerouter.data.screens.device_manager.model.load

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.common.screen_components.DeviceModel
import com.tfg.securerouter.data.router.sendCommand
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
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(" ")
                if (parts.size >= 7) {
                    val statusFlag = parts[parts.size - 2]
                    val blockedFlag = parts[parts.size - 1]

                    val labels = mutableSetOf<DeviceLabel>()
                    labels += if (statusFlag == "1") DeviceLabel.Online else DeviceLabel.Offline

                    if (blockedFlag == "1") {
                        labels += DeviceLabel.Blocked
                    }

                    val vendorName = getDeviceType(parts[1])
                    val (iconRes, extraLabel) = getDeviceIconAndType(vendorName)
                    extraLabel?.let { labels += it }

                    DeviceModel(
                        mac = parts[1],
                        ip = parts[2],
                        hostname = parts[3],
                        icon = iconRes,
                        labels = labels
                    )
                } else {
                    null
                }
            }
    }
}
