package com.tfg.securerouter.ui.app.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.R
import com.tfg.securerouter.data.navegation.LocalNavController
import com.tfg.securerouter.data.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.screens.settings.SettingsCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.settings.components.LanguageComponent

class SettingsScreen: ScreenDefault() {
    @Composable
    @Override
    fun SetingsScreenInit() {
        val coordinator: SettingsCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val settingsCoordinator = coordinator as? SettingsCoordinator
            ?: throw IllegalArgumentException("Expected SettingCoordinator")

        addComponents(
            { LanguageComponent(
                currentLanguage = stringResource(R.string.setting_language_selected),
                navController = LocalNavController.current
            ) }
        )

        RenderScreen()
    }
}
