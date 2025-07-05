package com.tfg.securerouter.ui.app.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.tfg.securerouter.ui.app.screens.home.components.ConnectedDevicesList
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.icons.RouterIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.menu.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.menu.screens.home.HomeCoordinator
import com.tfg.securerouter.data.menu.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.menu.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.data.menu.screens.home.model.send.SendRouterName
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.common.texts.TextWithToggleOption

class HomeScreen: ScreenDefault() {
    @Composable
    fun HomeScreenInit() {
        val coordinator: HomeCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val homeCoordinator = coordinator as? HomeCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

        val routerInfoModel = homeCoordinator.modules.filterIsInstance<HomeRouterInfoModel>().first()
        val connectedDevicesModel = homeCoordinator.modules.filterIsInstance<ConnectedDeviceModel>().first()

        val routerState = routerInfoModel.state.collectAsState().value
        val devicesState = connectedDevicesModel.state.collectAsState().value

        addComponents(
            {HomeRouterInfoSection(
                state = routerState,
                onEditAliasClick = { newAlias ->
                    SendRouterName.updateRouterAlias(newAlias) { success ->
                        println("Alias actualizado: $success")
                    }
                }
            )},
            { RouterIcon() },
            { ConnectedDevicesList(devices_state = devicesState, weight = 0.4f) },
            { TextWithToggleOption(text = "Prueba:", initialChecked = true, onToggleChanged = { /* TODO */ }) }
        )

        RenderScreen()
    }
}
