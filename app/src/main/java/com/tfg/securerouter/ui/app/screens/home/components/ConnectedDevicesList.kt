package com.tfg.securerouter.ui.common.home_screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.menu.screens.home.state.ConnectedDeviceState

@Composable
fun ConnectedDevicesList(devices_state: ConnectedDeviceState) {
    val devices = devices_state.connectedDevices

    Text("${stringResource(
        id = R.string.home_connected_devices_list)} (${devices.size})",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
        items(devices) { device ->
            DeviceCard(device, onClick = { /* TODO */ })
        }
    }
}