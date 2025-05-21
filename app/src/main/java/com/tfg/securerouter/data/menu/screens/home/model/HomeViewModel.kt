package com.tfg.securerouter.data.menu.screens.home.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.menu.screens.home.state.HomeUiState
import com.tfg.securerouter.data.router.sendCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * HomeViewModel is responsible for managing the UI-related data for the Home screen.
 *
 * Responsibilities:
 * - Fetches and parses router information (SSID, IP, MAC address).
 * - Retrieves the list of currently connected devices from the router.
 * - Maintains a reactive state using [StateFlow], observed by the UI layer.
 *
 * Architecture:
 * - Uses Jetpack ViewModel to survive configuration changes.
 * - Uses coroutines for asynchronous IO-bound operations.
 * - Follows clean architecture by separating logic from UI.
 *
 * @property uiState Publicly exposed immutable state observed by the UI.
 *
 * @see HomeUiState
 */
class HomeViewModel : ViewModel() {

    // Internal mutable state representing the UI data
    private val _uiState = MutableStateFlow(
        HomeUiState(
            routerAlias = null,
            routerIp = "",
            macAddress = "",
            connectedDevices = emptyList(),
        )
    )

    // Exposed immutable state that the UI can observe
    val uiState: StateFlow<HomeUiState> = _uiState

    // Automatically loads the connected devices when the ViewModel is created
    init {
        loadConnectedDevices()
    }

    /**
     * Loads the router configuration and connected devices asynchronously.
     *
     * - Sends shell commands to the router to retrieve `/etc/config/wireless` and `/tmp/dhcp.leases`.
     * - Parses the responses and updates the [HomeUiState].
     */
    fun loadConnectedDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            // Set loading state to true
            _uiState.update { it.copy(isLoading = true) }

            // Launch two async tasks:
            // 1. Get wireless configuration
            val wirelessDeferred = async {
                val output = sendCommand("cat /etc/config/wireless")
                parseWirelessConfig(output)
            }

            // 2. Get DHCP leases (connected devices)
            val devicesDeferred = async {
                val output = sendCommand("cat /tmp/dhcp.leases")
                parseLeasesDevices(output)
            }

            // Wait for both results
            val routerState = wirelessDeferred.await()
            val connectedDevices = devicesDeferred.await()

            // Update UI state with parsed values
            _uiState.update {
                it.copy(
                    routerAlias = routerState.routerAlias,
                    routerIp = routerState.routerIp,
                    macAddress = routerState.macAddress,
                    connectedDevices = connectedDevices,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Parses the wireless configuration output from the router.
     *
     * @param output The raw string output from `cat /etc/config/wireless`.
     * @return A partial [HomeUiState] containing:
     *   - routerAlias: SSID of the router (nullable)
     *   - routerIp: Identified IP or fallback "Unknown"
     *   - macAddress: First MAC address found
     */
    fun parseWirelessConfig(output: String): HomeUiState {
        var deviceIp: String? = null
        var ssid: String? = null
        var macAddress: String = ""

        val lines = output.lines()
        for (i in lines.indices) {
            val line = lines[i].trim()
            if (line.startsWith("config wifi-device")) {
                // Extract device identifier or IP if available
                deviceIp = line.substringAfter("'").substringBefore("'")
            } else if (line.startsWith("option ssid")) {
                // Extract SSID (router alias)
                ssid = line.substringAfter("'").substringBefore("'")
            } else if (line.startsWith("list maclist")) {
                // Extract the first MAC address listed
                macAddress = line.substringAfter("'").substringBefore("'")
            }
        }

        return HomeUiState(
            routerAlias = ssid,
            routerIp = deviceIp ?: "Unknown",
            macAddress = macAddress
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
    fun parseLeasesDevices(output: String): List<DeviceModel> {
        println(output) // Debug print, can be removed in production
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
