package com.tfg.securerouter.data.app.screens.other_screen

import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.menu.ScreenOptions
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen

/**
 * Implementation of [ScreenOptions] representing the Language Selection screen.
 *
 * This object defines:
 * - The navigation route for accessing the screen.
 * - The composable content to render when the route is selected.
 *
 * It connects the `LanguageScreen` UI to the app's navigation system, making it
 * available as a selectable option in the drawer or other navigation components.
 *
 * @property route The unique route identifier used for navigation to this screen.
 *
 * @see ScreenOptions
 * @see LanguageScreen
 */
object LanguageScreenOption : ScreenOptions {
    override val route = "language_selection"

    /**
     * Composable content for the Language Selection screen.
     *
     * This method initializes and renders the [LanguageScreen] UI when navigated to.
     */
    @Composable
    override fun Content() {
        val language = LanguageScreen()
        language.LanguageScreenInit()
    }
}