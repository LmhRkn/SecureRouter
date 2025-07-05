package com.tfg.securerouter.ui.app.screens.device_manager.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.menu.screens.device_manager.state.HistoricalDeviceState
import com.tfg.securerouter.data.menu.screens.home.model.load.HistoricalDeviceModel
import com.tfg.securerouter.data.menu.screens.home.state.ConnectedDeviceState
import com.tfg.securerouter.data.utils.height_weight_to_dp
import com.tfg.securerouter.ui.common.screen_components.devices.DeviceList

@Composable
fun BlockedDevicesList(
    devices_state: HistoricalDeviceState,
    weight: Float = 1f
) {
    // Filtrar dispositivos que NO tienen la etiqueta Blocked
    val devices = devices_state.historicalDevices.filter {
        DeviceLabel.Blocked in it.labels
    }

    BoxWithConstraints {
        val heightDp = height_weight_to_dp(maxHeight = maxHeight, weight = weight)

        Column(
            modifier = Modifier
                .height(heightDp)
                .fillMaxWidth()
        ) {
            Text(
                "${
                    stringResource(id = R.string.dive_manger_blocked_devices_list)
                } (${devices.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            DeviceList(devices = devices, max_size = heightDp)
        }
    }
}
