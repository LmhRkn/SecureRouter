package com.tfg.securerouter.ui.app.screens.wifi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceTimesRuleModel
import com.tfg.securerouter.data.app.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.app.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.data.app.screens.home.model.send.SendRouterName
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiFilterWebRuleModel
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiRouterInfoModel
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiTimesRuleModel
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.wifi.components.WifiOptionsFilterWeb
import com.tfg.securerouter.ui.app.screens.wifi.components.WifiOptionsTimes
import com.tfg.securerouter.ui.app.screens.wifi.components.WifiRouterInfoSection
import com.tfg.securerouter.ui.app.screens.wifi.components.WifiRouterPassword

/**
 * Composable screen for managing Wi-Fi settings in the SecureRouter app.
 *
 * This screen renders the Wi-Fi UI, currently showing a placeholder text.
 * Future versions should include components for viewing and modifying Wi-Fi
 * configuration (SSID, password, security mode, etc.).
 *
 * It uses [WifiCoordinator] as the screen coordinator for handling Wi-Fi-related
 * state and business logic.
 *
 * @see WifiCoordinator
 * @see ScreenDefault
 */
class WifiScreen: ScreenDefault() {
    /**
     * Initializes the Wi-Fi screen by instantiating its ViewModel and
     * delegating to [ScreenInit] for lifecycle management.
     *
     * This is the entry point for the screen.
     *
     * @see WifiCoordinator
     */
    @Composable
    @Override
    fun WifiScreenInit() {
        val coordinator: WifiCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    /**
     * Renders the UI content for the Wi-Fi screen.
     *
     * This function:
     * - Validates that the provided [ScreenCoordinatorDefault] is a [WifiCoordinator].
     * - Adds UI components to the layout using [addComponents].
     * - Calls [RenderScreen] to display the composed content.
     *
     * Currently displays a placeholder text ("Wi-Fi").
     * Replace with actual Wi-Fi management UI components as needed.
     *
     * @param coordinator The screen coordinator, expected to be a [WifiCoordinator].
     * @throws IllegalArgumentException if the provided coordinator is not of the expected type.
     *
     * @see WifiCoordinator
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val wifiCoordinator = coordinator as? WifiCoordinator
            ?: throw IllegalArgumentException("Expected WifiCoordinator")

        val routerInfoModel = wifiCoordinator.modules.filterIsInstance<WifiRouterInfoModel>().first()
        val routerState = routerInfoModel.state.collectAsState().value

        val wifiTimesRuleModel = wifiCoordinator.modules
            .filterIsInstance<WifiTimesRuleModel>()
            .first()

        val wifiTimesRule = wifiTimesRuleModel.state.collectAsState().value

        val wifiFilterWebRuleModel = wifiCoordinator.modules
            .filterIsInstance<WifiFilterWebRuleModel>()
            .first()

        val wifiFilterWebRule = wifiFilterWebRuleModel.state.collectAsState().value

        setComponents(
            { WifiRouterInfoSection(
                state = routerState,
                onEditAliasClick = { newAlias ->
                    SendRouterName.updateRouterAlias(routerState.wirelessName, newAlias)
                }
            )},
            { WifiRouterPassword(routerState) },
            { WifiOptionsTimes(wifiTimesRule) },
            { WifiOptionsFilterWeb(wifiFilterWebRule) },
        )

        RenderScreen()
    }
}
