package com.tfg.securerouter.data.app.screens.device_manager.model.load

import com.tfg.securerouter.data.app.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.DeviceModel
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.device_manager.state.HistoricalDeviceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * View model responsible for loading and managing the historical list of connected devices.
 *
 * Usage:
 * This model retrieves device data from the router (via the `dhcp.leases` file), parses it into
 * a list of [DeviceModel], and exposes it through a [StateFlow] for reactive UI updates.
 *
 * It extends [ScreenComponentModelDefault] to integrate with screen lifecycle management.
 *
 * @property sharedCache A shared in-memory cache used to store raw command outputs.
 * @property state A [StateFlow] exposing the current [HistoricalDeviceState] for UI consumption.
 *
 * @see HistoricalDeviceState
 * @see DeviceModel
 * @see safeLoad
 */
class HistoricalDeviceModel(
    private val sharedCache: MutableMap<String, Any>,
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
     * Parses the raw output from the router's `dhcp.leases` file into a list of [DeviceModel] objects.
     *
     * Each non-empty line in the output is expected to follow this format:
     * ```
     * <timestamp> <MAC> <IP> <hostname> <unknown1>
     * ```
     *
     * The parser assigns appropriate [DeviceLabel]s based on these flags and infers
     * the device type and icon from the vendor information (via [getDeviceType] and [getDeviceIconAndType]).
     *
     * Devices failing to match the expected format (less than 7 fields) are skipped.
     *
     * @param output The raw string output from the router command.
     * @return A list of [DeviceModel] instances representing parsed devices.
     *
     * @see DeviceModel
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
                    val (iconRes, iconDesc, extraLabel) = getDeviceIconAndType(vendorName)
                    extraLabel?.let { labels += it }

                    DeviceModel(
                        mac = parts[1],
                        ip = parts[2],
                        hostname = parts[3],
                        icon = iconRes,
                        iconDescription = iconDesc,
                        labels = labels
                    )
                } else {
                    null
                }
            }
    }
}
