package com.tfg.securerouter.data.app.screens.other_screen

import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.menu.ScreenOptions
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen
import com.tfg.securerouter.ui.app.screens.sh_login.ShScreen

object ShLoginScreenOption : ScreenOptions {
    override val route = "sh_login_selection"

    @Composable
    override fun Content() {
        val shLogin = ShScreen()
        shLogin.ShLoginScreenInit()
    }
}