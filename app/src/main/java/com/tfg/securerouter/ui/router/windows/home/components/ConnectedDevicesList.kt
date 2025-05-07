package com.tfg.securerouter.ui.router.windows.home.components

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
import com.tfg.securerouter.ui.router.windows.home.model.DeviceModel

@Composable
fun ConnectedDevicesList(devices: List<DeviceModel>) {
    Text("${stringResource(id = R.string.home_connected_devices_list)} (${devices.size})", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
        items(devices) { device ->
            DeviceCard(device, onClick = { /* TODO */ })
        }
    }
}