package com.tfg.securerouter.ui.app.screens.wifi

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault

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
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val filterCoordinator = coordinator as? WifiCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

        addComponents(
            { Text("Wifi", color=Color.Black) }
        )

        RenderScreen()
    }
}
