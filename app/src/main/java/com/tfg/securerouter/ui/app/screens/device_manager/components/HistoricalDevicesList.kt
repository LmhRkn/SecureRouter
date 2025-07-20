package com.tfg.securerouter.ui.app.screens.device_manager.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.app.screens.device_manager.model.DeviceManagerScreenEvent
import com.tfg.securerouter.data.app.screens.device_manager.state.HistoricalDeviceState
import com.tfg.securerouter.data.utils.height_weight_to_dp
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.common.screen_components.devices.DeviceList

/**
 * Composable for displaying a filtered and searchable list of historical (allowed) devices.
 *
 * Features:
 * - Excludes devices labeled [DeviceLabel.Blocked].
 * - Filters devices based on a search query and selected labels.
 * - Reacts to toggle events from the parent screen to show or hide the list.
 * - Dynamically adjusts height proportionally using [height_weight_to_dp].

 * @param devices_state The [HistoricalDeviceState] containing all historical devices.
 * @param weight Proportional height (0.0f to 1.0f) relative to available space. Defaults to `1f`.
 * @param parent The parent [ScreenDefault] providing access to the event bus.
 *
 * @see DeviceList
 * @see DeviceManagerScreenEvent
 */
@Composable
fun HistoricalDevicesList(
    devices_state: HistoricalDeviceState,
    weight: Float = 1f,
    parent: ScreenDefault
) {
    val eventFlow = parent.eventBus
    var searchQuery by remember { mutableStateOf("") }
    var labelFilters by remember { mutableStateOf(emptySet<DeviceLabel>()) }

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            if (event is DeviceManagerScreenEvent.SearchSomething) {
                searchQuery = event.query
            } else if (event is DeviceManagerScreenEvent.FilterSomething) {
                labelFilters = event.filters
            }
        }
    }

    // Filtrar dispositivos que NO tienen la etiqueta Blocked
    val total_devices = devices_state.historicalDevices.filter {
        DeviceLabel.Blocked !in it.labels
    }

    val devices = total_devices.filter { device ->
        val hostname = device.hostname?.lowercase() ?: ""
        val query = searchQuery.lowercase()

        val matchesQuery = hostname.contains(query)
        val matchesLabels = if (labelFilters.isEmpty()) {
            true
        } else {
            labelFilters.all { it in device.labels }
        }

        matchesQuery && matchesLabels
    }


    BoxWithConstraints {
        val heightDp = height_weight_to_dp(maxHeight = maxHeight, weight = weight)
        var showAllowedDevices by remember { mutableStateOf(true) }

        // Escuchar el evento Toggle
        LaunchedEffect(Unit) {
            eventFlow.collect { event ->
                if (event is DeviceManagerScreenEvent.ToggleSomething) {
                    showAllowedDevices = !showAllowedDevices
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (showAllowedDevices) Modifier.height(heightDp)
                    else Modifier.wrapContentHeight()
                )
        ) {
            Text(
                "${stringResource(id = R.string.dive_manger_historical_devices_list)} (${total_devices.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (showAllowedDevices) {
                DeviceList(devices = devices, maxSize = heightDp)
            }
        }
    }
}
