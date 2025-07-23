package com.tfg.securerouter.ui.app.screens.device_manager

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.device_manager.DeviceManagerCoordinator
import com.tfg.securerouter.data.app.screens.device_manager.model.load.HistoricalDeviceModel
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.device_manager.components.BlockedDevicesList
import com.tfg.securerouter.ui.app.screens.device_manager.components.ButtonToggleList
import com.tfg.securerouter.ui.app.screens.device_manager.components.DeviceManagerSearchBar
import com.tfg.securerouter.ui.app.screens.device_manager.components.HistoricalDevicesList

/**
 * Composable screen for managing connected and blocked devices.
 *
 * This screen renders the Device Manager UI, displaying:
 * - A search bar for filtering devices.
 * - A list of currently connected devices.
 * - A toggle button to switch between lists.
 * - A list of blocked devices.
 *
 * It uses [DeviceManagerCoordinator] as the screen coordinator for loading data
 * and managing shared state across components.
 *
 * @see DeviceManagerCoordinator
 * @see ScreenDefault
 */
class DeviceManagerScreen: ScreenDefault() {
    /**
     * Initializes the Device Manager screen by instantiating its ViewModel and
     * delegating to [ScreenInit] for lifecycle management.
     *
     * This is the entry point for the screen.
     *
     * @see DeviceManagerCoordinator
     */
    @Composable
    @Override
    fun DeviceManagerScreenInit() {
        val coordinator: DeviceManagerCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    /**
     * Renders the UI content for the Device Manager screen.
     *
     * This function:
     * - Retrieves the [HistoricalDeviceModel] module from the coordinator.
     * - Collects its state via [collectAsState].
     * - Adds UI components (search bar, device lists, toggle button) using [addComponents].
     *
     * @param coordinator The screen coordinator, expected to be a [DeviceManagerCoordinator].
     * @throws IllegalArgumentException if the provided coordinator is not of the expected type.
     *
     * @see DeviceManagerCoordinator
     */
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val deviceManagerCoordinator = coordinator as? DeviceManagerCoordinator
            ?: throw IllegalArgumentException("Expected DeviceManagerCoordinator")

        val connectedDevicesModel = deviceManagerCoordinator.modules.filterIsInstance<HistoricalDeviceModel>().first()

        val devicesState = connectedDevicesModel.state.collectAsState().value


        addComponents(
            {
                DeviceManagerSearchBar(parent = this@DeviceManagerScreen)
                HistoricalDevicesList(devices_state = devicesState, weight = 0.5f, parent = this@DeviceManagerScreen)
                ButtonToggleList(parent = this@DeviceManagerScreen)
                Spacer(modifier = Modifier.height(8.dp))
                BlockedDevicesList(devicesState = devicesState, weight = 0.5f, parent = this@DeviceManagerScreen)
            },
        )

        RenderScreen()
    }
}
