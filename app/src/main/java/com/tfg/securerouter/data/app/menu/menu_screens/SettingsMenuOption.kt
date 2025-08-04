package com.tfg.securerouter.data.app.menu.menu_screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.ui.app.screens.settings.SettingsScreen

/**
 * Object representing the "Configuration" menu option.
 *
 * This screen is part of the main navigation drawer and allows the user to manage connected devices.
 * It implements the [MenuOption] interface, which defines the required properties for dynamic menu entries.
 *
 * @constructor This is a singleton object; it is not instantiated directly.
 *
 * @see MenuOption for the base interface.
 */

object SettingsMenuOption : MenuOption {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
    override val titleResId = R.string.settings_title

    @Composable
    override fun Content() {
        val settings = SettingsScreen()
        settings.SettingsScreenInit()

    }
}