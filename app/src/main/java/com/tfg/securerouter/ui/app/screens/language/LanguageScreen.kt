package com.tfg.securerouter.ui.app.screens.language

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.language.LanguageCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.language.components.LanguageButtons
import com.tfg.securerouter.ui.app.screens.language.components.LanguageList

/**
 * Composable screen for managing language settings in the SecureRouter app.
 *
 * This screen renders the Language UI, currently showing a placeholder text.
 * Future versions should include a list of available languages and allow
 * the user to change the app’s language preference.
 *
 * It uses [LanguageCoordinator] as the screen coordinator to manage state
 * and handle language-related actions.
 *
 * @see LanguageCoordinator
 * @see ScreenDefault
 */
class LanguageScreen: ScreenDefault() {
    /**
     * Initializes the Language screen by instantiating its ViewModel and
     * delegating to [ScreenInit] for lifecycle management.
     *
     * This is the entry point for the screen.
     *
     * @see LanguageCoordinator
     */
    @Composable
    @Override
    fun LanguageScreenInit() {
        val coordinator: LanguageCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    /**
     * Renders the UI content for the Language screen.
     *
     * This function:
     * - Validates that the provided [ScreenCoordinatorDefault] is a [LanguageCoordinator].
     * - Adds UI components to the layout using [addComponents].
     * - Calls [RenderScreen] to display the composed content.
     *
     * Currently displays a placeholder text ("Language").
     * Replace with actual language selection UI components as needed.
     *
     * @param coordinator The screen coordinator, expected to be a [LanguageCoordinator].
     * @throws IllegalArgumentException if the provided coordinator is not of the expected type.
     *
     * @see LanguageCoordinator
     */
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val languageCoordinator = coordinator as? LanguageCoordinator
            ?: throw IllegalArgumentException("Expected LanguageCoordinator")

        addComponents(
            {
                LanguageList(this)
                LanguageButtons(this)
            },
        )

        RenderScreen()
    }
}
