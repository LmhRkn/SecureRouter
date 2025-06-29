package com.tfg.securerouter.ui.app.screens.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.menu.screens.settings.SettingsCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault

class SettingsScreen: ScreenDefault() {
    @Composable
    @Override
    fun SetingsScreenInit() {
        val coordinator: SettingsCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val settingsCoordinator = coordinator as? SettingsCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

//        val routerInfoModel = ScreenContent.modules.filterIsInstance<HomeRouterInfoModel>().first()
//        val connectedDevicesModel = ScreenContent.modules.filterIsInstance<ConnectedDeviceModel>().first()

//        val routerState = routerInfoModel.state.collectAsState().value
//        val devicesState = connectedDevicesModel.state.collectAsState().value

        addComponents(
            { Text("Settings", color=Color.Black) }
        )

        RenderScreen()
    }
}
