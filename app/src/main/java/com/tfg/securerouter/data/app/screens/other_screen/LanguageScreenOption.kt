package com.tfg.securerouter.data.app.screens.other_screen

import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.menu.ScreenOptions
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen

object LanguageScreenOption : ScreenOptions {
    override val route = "language_selection"

    @Composable
    override fun Content() {
        val language = LanguageScreen()
        language.AdministrarScreenInit()
    }
}