package com.tfg.securerouter.ui.app.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.notice.model.tutorials.AutoOpenTutorialOnce
import com.tfg.securerouter.data.app.notice.utils.PromptHost
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.home.tutorials.RegisterSettingsTutorial
import com.tfg.securerouter.data.app.screens.settings.SettingsCoordinator
import com.tfg.securerouter.data.app.screens.wifi.tutorials.WifiTutorial
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialModal
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
        val tutorialSpec by TutorialCenter.spec.collectAsState()
        val tutorialOpen by TutorialCenter.open.collectAsState()

        PromptHost()
        AutoOpenTutorialOnce(
            routerId = AppSession.routerId,
            screenKey = HomeMenuOption.route
        )

        RegisterSettingsTutorial()
        setComponents(
            {
                LanguageComponent(
                    navController = LocalNavController.current
                )
            }
        )

        Box(Modifier.fillMaxSize()) {
            RenderScreen()

            if (tutorialOpen && tutorialSpec != null) {
                TutorialModal(
                    spec = tutorialSpec!!,
                    onSkip = { TutorialCenter.close() },
                    onFinish = { TutorialCenter.close() }
                )
            }
        }
    }
}
