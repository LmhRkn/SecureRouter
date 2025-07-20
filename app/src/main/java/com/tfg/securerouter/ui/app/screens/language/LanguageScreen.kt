package com.tfg.securerouter.ui.app.screens.language

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.language.LanguageCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault

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
