package com.tfg.securerouter.data.screens.home.model.load

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.common.screen_components.DeviceModel
import com.tfg.securerouter.data.menu.screens.home.state.ConnectedDeviceState
import com.tfg.securerouter.data.router.sendCommand
import com.tfg.securerouter.data.screens.ScreenComponentModelDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class ConnectedDeviceModel(
    private val sharedCache: MutableMap<String, Any>,
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

    private fun parseDevices(output: String): List<DeviceModel> {
        return output.lines()
            .filter { it.isNotBlank() } // Remove empty lines
            .mapNotNull { line ->
                val parts = line.split(" ")

                val vendorName = getDeviceType(parts[1])
                val (iconRes, extraLabel) = getDeviceIconAndType(vendorName)

                if (parts.size >= 4) {
                    // Parse and return device info
                    DeviceModel(
                        mac = parts[1],
                        ip = parts[2],
                        hostname = parts[3],
                        icon = iconRes
                    )
                } else {
                    null // Skip malformed lines
                }
            }
    }
}
