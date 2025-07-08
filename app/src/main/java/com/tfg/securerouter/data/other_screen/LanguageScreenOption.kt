package com.tfg.securerouter.data.other_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.runtime.Composable
import com.tfg.securerouter.R
import com.tfg.securerouter.data.menu.MenuOption
import com.tfg.securerouter.data.menu.ScreenOptions
import com.tfg.securerouter.ui.app.screens.device_manager.DeviceManagerScreen
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen

object LanguageScreenOption : ScreenOptions {
    override val route = "language_selection"

    @Composable
    override fun Content() {
        val language = LanguageScreen()
        language.AdministrarScreenInit()
    }
}