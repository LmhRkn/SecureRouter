package com.tfg.securerouter.ui.app.screens.language

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.screens.language.LanguageCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.device_manager.components.BlockedDevicesList
import com.tfg.securerouter.ui.app.screens.device_manager.components.ButtonToggleList
import com.tfg.securerouter.ui.app.screens.device_manager.components.DeviceManagerSearchBar
import com.tfg.securerouter.ui.app.screens.device_manager.components.HistoricalDevicesList
import com.tfg.securerouter.ui.common.searchbar.SearchBar

class LanguageScreen: ScreenDefault() {
    @Composable
    @Override
    fun AdministrarScreenInit() {
        val coordinator: LanguageCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val languageCoordinator = coordinator as? LanguageCoordinator
            ?: throw IllegalArgumentException("Expected LanguageCoordinator")

        addComponents(
            {
                Text("Language",
                    color = Color.Black)
            },
        )

        RenderScreen()
    }
}
