package com.tfg.securerouter.ui.app.screens.wifi

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.menu.screens.wifi.WifiCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault

class WifiScreen: ScreenDefault() {
    @Composable
    @Override
    fun WifiScreenInit() {
        val coordinator: WifiCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val filterCoordinator = coordinator as? WifiCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

//        val routerInfoModel = ScreenContent.modules.filterIsInstance<HomeRouterInfoModel>().first()
//        val connectedDevicesModel = ScreenContent.modules.filterIsInstance<ConnectedDeviceModel>().first()

//        val routerState = routerInfoModel.state.collectAsState().value
//        val devicesState = connectedDevicesModel.state.collectAsState().value

        addComponents(
            { Text("Wifi", color=Color.Black) }
        )

        RenderScreen()
    }
}
