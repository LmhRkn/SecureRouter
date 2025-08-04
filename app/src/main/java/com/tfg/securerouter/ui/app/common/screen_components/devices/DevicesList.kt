package com.tfg.securerouter.ui.app.common.screen_components.devices

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.ui.common.home_screen.DeviceCard

/**
 * Composable function for displaying a vertical list of devices in a grid layout.
 *
 * Usage:
 * This component renders a scrollable list of [DeviceCard] elements, each representing
 * a [DeviceModel]. It is typically used in the UI to present connected devices,
 * router devices, or similar collections.
 *
 * The grid is constrained to a fixed height of [maxSize] to avoid occupying unlimited space.
 * Each device is displayed as a single column (one card per row).
 *
 * @param devices The list of [DeviceModel] objects to be displayed.
 * @param max_size The maximum height of the grid in [Dp]. Defaults to `500.dp`.
 *
 * @see DeviceCard
 */
@Composable
fun DeviceList(
    devices: List<DeviceModel>,
    maxSize: Dp = 500.dp,
) {
    val navController: NavController = LocalNavController.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .height(maxSize)
    ) {
        items(devices) { device ->
            DeviceCard(device, onClick = {
                navController.navigate("devices_options_selection/${device.mac}")
            })
        }
    }
}
