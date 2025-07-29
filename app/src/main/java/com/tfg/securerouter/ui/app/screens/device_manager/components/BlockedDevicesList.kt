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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.screens.device_manager.model.DeviceManagerScreenEvent
import com.tfg.securerouter.data.app.screens.device_manager.state.HistoricalDeviceState
import com.tfg.securerouter.data.utils.height_weight_to_dp
import com.tfg.securerouter.ui.app.common.screen_components.devices.DeviceList
import com.tfg.securerouter.ui.app.screens.ScreenDefault

/**
 * Composable for displaying a filtered and searchable list of blocked devices.
 *
 * Features:
 * - Dynamically filters devices by search query and selected labels.
 * - Reacts to events from the parent screen’s [ScreenDefault.eventBus], supporting:
 *   - Search queries ([DeviceManagerScreenEvent.SearchSomething])
 *   - Label filters ([DeviceManagerScreenEvent.FilterSomething])
 *   - Toggling visibility ([DeviceManagerScreenEvent.ToggleSomething]).
 * - Adjusts its height proportionally using [height_weight_to_dp].
 *
 * @param devicesState The [HistoricalDeviceState] holding all historical devices.
 * @param weight Proportional height (0.0f to 1.0f) relative to available space. Defaults to `1f`.
 * @param parent The parent [ScreenDefault] providing access to the event bus.
 *
 * @see DeviceList
 * @see DeviceManagerScreenEvent
 */
@Composable
fun BlockedDevicesList(
    devicesState: HistoricalDeviceState,
    weight: Float = 1f,
    parent: ScreenDefault
) {
    val eventFlow = parent.eventBus
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var labelFilters by rememberSaveable { mutableStateOf(emptySet<DeviceLabel>()) }

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            if (event is DeviceManagerScreenEvent.SearchSomething) {
                searchQuery = event.query
            } else if (event is DeviceManagerScreenEvent.FilterSomething) {
                labelFilters = event.filters
            }
        }
    }
    val totalDevices = devicesState.historicalDevices.filter {
        DeviceLabel.Blocked in it.labels
    }

    val devices = totalDevices.filter { device ->
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

        var showAllowedDevices by rememberSaveable { mutableStateOf(true) }

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
                    if (!showAllowedDevices) Modifier.height(heightDp)
                    else Modifier.wrapContentHeight()
                )
        ) {
            Text(
                "${
                    stringResource(id = R.string.dive_manger_blocked_devices_list)
                } (${totalDevices.size})",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!showAllowedDevices) DeviceList(devices = devices, maxSize = heightDp)
        }
    }
}
