package com.tfg.securerouter.ui.common.screen_components.devices

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.common.screen_components.DeviceModel
import com.tfg.securerouter.ui.common.home_screen.DeviceCard

@Composable
fun DeviceList(
    devices: List<DeviceModel>,
    max_size: Dp = 500.dp
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .height(max_size)
    ) {
        items(devices) { device ->
            DeviceCard(device, onClick = { /* TODO */ })
        }
    }
}
