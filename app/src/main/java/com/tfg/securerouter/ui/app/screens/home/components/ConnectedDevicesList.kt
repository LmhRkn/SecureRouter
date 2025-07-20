package com.tfg.securerouter.ui.app.screens.home.components

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
import com.tfg.securerouter.data.app.screens.home.state.load.ConnectedDeviceState
import com.tfg.securerouter.data.utils.height_weight_to_dp
import com.tfg.securerouter.ui.app.common.screen_components.devices.DeviceList

@Composable
fun ConnectedDevicesList(
    devices_state: ConnectedDeviceState,
    weight: Float = 1f
) {
    val devices = devices_state.connectedDevices

    BoxWithConstraints {
        val heightDp = height_weight_to_dp(maxHeight = maxHeight, weight = weight)

        Column(
            modifier = Modifier
                .height(heightDp)
                .fillMaxWidth()
        ) {
            Text(
                "${
                    stringResource(
                        id = R.string.home_connected_devices_list
                    )
                } (${devices.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            DeviceList(devices = devices, maxSize = heightDp)
        }
    }
}