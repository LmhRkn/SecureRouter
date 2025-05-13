package com.tfg.securerouter.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.router.sendCommand
import com.tfg.securerouter.data.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            routerAlias = null,
            routerIp = "",
            macAddress = "",
            connectedDevices = emptyList()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadConnectedDevices()
    }

    fun loadConnectedDevices() {
        viewModelScope.launch(Dispatchers.IO) {
            val wirelessDeferred = async {
                val output = sendCommand(
                    command = "cat /etc/config/wireless"
                )
                parseWirelessConfig(output)
            }

            val devicesDeferred = async {
                val output = sendCommand(
                    command = "cat /tmp/dhcp.leases"
                )
                parseLeasesDevices(output)
            }

            val routerState = wirelessDeferred.await()
            val connectedDevices = devicesDeferred.await()

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


    fun parseWirelessConfig(output: String): HomeUiState {
        var deviceIp: String? = null
        var ssid: String? = null
        var macAddress: String = ""

        val lines = output.lines()
        for (i in lines.indices) {
            val line = lines[i].trim()
            if (line.startsWith("config wifi-device")) {
                // Extraer el nombre del dispositivo, ej: 'radio0'
                deviceIp = line.substringAfter("'").substringBefore("'")
            } else if (line.startsWith("option ssid")) {
                ssid = line.substringAfter("'").substringBefore("'")
            } else if (line.startsWith("list maclist")) {
                macAddress = line.substringAfter("'").substringBefore("'")
            }
        }

        return HomeUiState(
            routerAlias = ssid, // puedes pedirle al usuario que lo defina
            routerIp = deviceIp ?: "Unknown",
            macAddress = macAddress
        )
    }


    fun parseLeasesDevices(output: String): List<DeviceModel> {
        println(output)
        return output.lines()
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(" ")
                if (parts.size >= 4) {
                    DeviceModel(
                        mac = parts[1],
                        ip = parts[2],
                        hostname = parts[3]
                    )
                } else {
                    null
                }
            }
    }
}
