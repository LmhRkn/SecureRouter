package com.tfg.securerouter.ui.app.screens.device_manager

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.menu.screens.device_manager.DevicemanagerCoordinator
import com.tfg.securerouter.data.menu.screens.home.model.load.HistoricalDeviceModel
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.device_manager.components.BlockedDevicesList
import com.tfg.securerouter.ui.app.screens.device_manager.components.ButtonToggleList
import com.tfg.securerouter.ui.app.screens.device_manager.components.DeviceManagerSearchBar
import com.tfg.securerouter.ui.app.screens.device_manager.components.HistoricalDevicesList
import com.tfg.securerouter.ui.common.searchbar.SearchBar

class DeviceManagerScreen: ScreenDefault() {
    @Composable
    @Override
    fun AdministrarScreenInit() {
        val coordinator: DevicemanagerCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val administrarDispositivosCoordinator = coordinator as? DevicemanagerCoordinator
            ?: throw IllegalArgumentException("Expected AdministrarDispCoordinator")

        val connectedDevicesModel = administrarDispositivosCoordinator.modules.filterIsInstance<HistoricalDeviceModel>().first()

        val devicesState = connectedDevicesModel.state.collectAsState().value


        addComponents(
            {
                DeviceManagerSearchBar(parent = this@DeviceManagerScreen)
                HistoricalDevicesList(devices_state = devicesState, weight = 0.5f, parent = this@DeviceManagerScreen)
                ButtonToggleList(parent = this@DeviceManagerScreen)
                Spacer(modifier = Modifier.height(8.dp))
                BlockedDevicesList(devices_state = devicesState, weight = 0.5f, parent = this@DeviceManagerScreen)
            },
        )

        RenderScreen()
    }
}
