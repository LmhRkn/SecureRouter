package com.tfg.securerouter.ui.app.screens.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.settings.SettingsCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.settings.components.LanguageComponent


/**
 * Composable screen for managing application settings in the SecureRouter app.
 *
 * This screen renders the Settings UI, currently providing:
 * - A language selection component allowing navigation to the Language screen.
 *
 * It uses [SettingsCoordinator] as the screen coordinator for handling settings-related
 * state and business logic.
 *
 * @see SettingsCoordinator
 * @see ScreenDefault
 */
class SettingsScreen : ScreenDefault() {

    /**
     * Composable screen for managing application settings in the SecureRouter app.
     *
     * This screen renders the Settings UI, currently providing:
     * - A language selection component allowing navigation to the Language screen.
     *
     * It uses [SettingsCoordinator] as the screen coordinator for handling settings-related
     * state and business logic.
     *
     * @see SettingsCoordinator
     * @see ScreenDefault
     */
    @Composable
    @Override
    fun SettingsScreenInit() {
        val coordinator: SettingsCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    /**
     * Renders the UI content for the Settings screen.
     *
     * This function:
     * - Validates that the provided [ScreenCoordinatorDefault] is a [SettingsCoordinator].
     * - Adds UI components to the layout using [addComponents].
     * - Calls [RenderScreen] to display the composed content.
     *
     * Components rendered:
     * - [LanguageComponent]: Shows the current language and navigates to the Language screen.
     *
     * @param coordinator The screen coordinator, expected to be a [SettingsCoordinator].
     * @throws IllegalArgumentException if the provided coordinator is not of the expected type.
     *
     * @see SettingsCoordinator
     * @see LanguageComponent
     */
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val settingsCoordinator = coordinator as? SettingsCoordinator
            ?: throw IllegalArgumentException("Expected SettingCoordinator")

        setComponents(
            {
                LanguageComponent(
                    navController = LocalNavController.current
                )
            }
        )

        RenderScreen()
    }
}
