package com.tfg.securerouter.ui.app.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.tfg.securerouter.ui.app.screens.home.components.ConnectedDevicesList
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.icons.RouterIcon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.home.HomeCoordinator
import com.tfg.securerouter.data.app.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.app.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.data.app.screens.home.model.send.SendRouterName
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.common.texts.TextWithToggleOption

/**
 * Composable screen for displaying the home dashboard of the SecureRouter app.
 *
 * This screen renders the Home UI, displaying:
 * - Router information (name, status, etc.).
 * - A list of currently connected devices.
 * - A router icon and a toggleable test component.
 *
 * It uses [HomeCoordinator] as the screen coordinator for managing state
 * and providing modules like [HomeRouterInfoModel] and [ConnectedDeviceModel].
 *
 * @see HomeCoordinator
 * @see ScreenDefault
 */
class HomeScreen: ScreenDefault() {
    /**
     * Initializes the Home screen by instantiating its ViewModel and
     * delegating to [ScreenInit] for lifecycle management.
     *
     * This is the entry point for the screen.
     *
     * @see HomeCoordinator
     */
    @Composable
    fun HomeScreenInit() {
        val coordinator: HomeCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    /**
     * Renders the UI content for the Home screen.
     *
     * This function:
     * - Retrieves the [HomeRouterInfoModel] and [ConnectedDeviceModel] modules from the coordinator.
     * - Collects their states via [collectAsState].
     * - Adds UI components to the layout using [addComponents].
     *
     * Components rendered:
     * - [HomeRouterInfoSection]: Displays router info with an editable alias.
     * - [RouterIcon]: A visual representation of the router.
     * - [ConnectedDevicesList]: Displays connected devices.
     * - [TextWithToggleOption]: Example of a toggleable text component.
     *
     * @param coordinator The screen coordinator, expected to be a [HomeCoordinator].
     * @throws IllegalArgumentException if the provided coordinator is not of the expected type.
     *
     * @see HomeCoordinator
     * @see HomeRouterInfoModel
     * @see ConnectedDeviceModel
     */
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
            { ConnectedDevicesList(devicesState = devicesState, weight = 0.4f) },
            { TextWithToggleOption(text = "Prueba:", initialChecked = true, onToggleChanged = { /* TODO */ }) }
        )

        RenderScreen()
    }
}
