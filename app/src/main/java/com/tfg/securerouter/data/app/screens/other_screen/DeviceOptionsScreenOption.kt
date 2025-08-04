package com.tfg.securerouter.data.app.screens.other_screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.tfg.securerouter.data.app.menu.ScreenOptions
import com.tfg.securerouter.ui.app.screens.devices_options.DevicesOptionsScreen
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen

object DeviceOptionsScreenOption : ScreenOptions {
    override val route = "devices_options_selection/{mac}"

    override val arguments = listOf(
        navArgument("mac") { type = NavType.StringType }
    )

    @Composable
    override fun Content() {
        val devicesOptions = DevicesOptionsScreen()
        devicesOptions.DevicesOptionsScreenInit()
    }
}