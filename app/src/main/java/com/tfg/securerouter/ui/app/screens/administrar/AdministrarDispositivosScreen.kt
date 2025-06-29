package com.tfg.securerouter.ui.app.screens.administrar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.tfg.securerouter.ui.common.home_screen.ConnectedDevicesList
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.icons.RouterIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.menu.screens.administrar.AdministrarDispCoordinator
import com.tfg.securerouter.data.menu.screens.home.HomeCoordinator
import com.tfg.securerouter.data.menu.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.menu.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.data.menu.screens.home.model.send.SendRouterName
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.common.texts.TextWithToggleOption

class AdministrarDispositivosScreen: ScreenDefault() {
    @Composable
    @Override
    fun AdministrarScreenInit() {
        val coordinator: AdministrarDispCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val administrarDispositivosCoordinator = coordinator as? AdministrarDispCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

//        val routerInfoModel = administrarDispositivosCoordinator.modules.filterIsInstance<HomeRouterInfoModel>().first()
//        val connectedDevicesModel = administrarDispositivosCoordinator.modules.filterIsInstance<ConnectedDeviceModel>().first()

//        val routerState = routerInfoModel.state.collectAsState().value
//        val devicesState = connectedDevicesModel.state.collectAsState().value

        addComponents(
            { Text("Administrar dispositivos", color=Color.Black) }
        )

        RenderScreen()
    }
}
