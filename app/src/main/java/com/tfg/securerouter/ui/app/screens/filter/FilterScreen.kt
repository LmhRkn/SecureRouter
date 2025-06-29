package com.tfg.securerouter.ui.app.screens.filter

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.menu.screens.filter.FilterCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault

class FilterScreen: ScreenDefault() {
    @Composable
    @Override
    fun FilterScreenInit() {
        val coordinator: FilterCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val filterCoordinator = coordinator as? FilterCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

//        val routerInfoModel = ScreenContent.modules.filterIsInstance<HomeRouterInfoModel>().first()
//        val connectedDevicesModel = ScreenContent.modules.filterIsInstance<ConnectedDeviceModel>().first()

//        val routerState = routerInfoModel.state.collectAsState().value
//        val devicesState = connectedDevicesModel.state.collectAsState().value

        addComponents(
            { Text("Filter", color=Color.Black) }
        )

        RenderScreen()
    }
}
