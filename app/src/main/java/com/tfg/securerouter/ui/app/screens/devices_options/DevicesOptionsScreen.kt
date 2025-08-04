package com.tfg.securerouter.ui.app.screens.devices_options

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.devices_options.DevicesOptionsCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsData

class DevicesOptionsScreen: ScreenDefault() {
    @Composable
    @Override
    fun DevicesOptionsScreenInit() {
        val coordinator: DevicesOptionsCoordinator = viewModel()

        ScreenInit(coordinator)
    }
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val navController = LocalNavController.current
        val mac = navController.currentBackStackEntry?.arguments?.getString("mac")

        val devicesOptionsCoordinator = coordinator as? DevicesOptionsCoordinator
            ?: throw IllegalArgumentException("ExpectedDevicesOptionsCoordinator")

        addComponents(
            { DeviceOptionsData(mac, {}) }
        )

        RenderScreen()
    }
}
