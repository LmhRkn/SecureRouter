package com.tfg.securerouter.ui.app.screens.wifi

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
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

        addComponents(
            { Text("Wifi", color=Color.Black) }
        )

        RenderScreen()
    }
}
