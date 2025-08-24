package com.tfg.securerouter.ui.app.screens.filter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.filter.FilterCoordinator
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiFilterWebRuleModel
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiTimesRuleModel
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.wifi.components.filters.WifiOptionsFilterWeb
import com.tfg.securerouter.ui.app.screens.wifi.components.filters.WifiOptionsTimes

/**
 * Composable screen for managing filters in the SecureRouter app.
 *
 * This screen renders the Filter UI, displaying:
 * - A placeholder text (to be replaced with actual filter UI components).
 *
 * It uses [FilterCoordinator] as the screen coordinator for handling state
 * and business logic related to filter management.
 *
 * @see FilterCoordinator
 * @see ScreenDefault
 */
class FilterScreen: ScreenDefault() {
    /**
     * Initializes the Filter screen by instantiating its ViewModel and
     * delegating to [ScreenInit] for lifecycle management.
     *
     * This is the entry point for the screen.
     *
     * @see FilterCoordinator
     */
    @Composable
    @Override
    fun FilterScreenInit() {
        val coordinator: FilterCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    /**
     * Renders the UI content for the Filter screen.
     *
     * This function:
     * - Validates that the provided [ScreenCoordinatorDefault] is a [FilterCoordinator].
     * - Adds UI components to the screen using [addComponents].
     * - Calls [RenderScreen] to display the composed content.
     *
     * @param coordinator The screen coordinator, expected to be a [FilterCoordinator].
     * @throws IllegalArgumentException if the provided coordinator is not of the expected type.
     *
     * @see FilterCoordinator
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val filterCoordinator = coordinator as? FilterCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")


        val wifiTimesRuleModel = filterCoordinator.modules
            .filterIsInstance<WifiTimesRuleModel>()
            .first()

        val wifiFilterWebRuleModel = filterCoordinator.modules
            .filterIsInstance<WifiFilterWebRuleModel>()
            .first()

        val wifiTimesRule = wifiTimesRuleModel.state.collectAsState().value
        val wifiFilterWebRule = wifiFilterWebRuleModel.state.collectAsState().value

        setComponents(
            {
                WifiOptionsTimes(wifiTimesRule)
                WifiOptionsFilterWeb(wifiFilterWebRule)
            },
        )

        RenderScreen()
    }

}
