package com.tfg.securerouter.data.app.menu.menu_screens

import HomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.data.app.menu.menu_screens.AdministrarDispositivosMenuOption.icon
import com.tfg.securerouter.data.app.menu.menu_screens.AdministrarDispositivosMenuOption.route
import com.tfg.securerouter.data.app.menu.menu_screens.AdministrarDispositivosMenuOption.titleResId

/**
 * Object representing the "Home" menu option.
 *
 * This screen is part of the main navigation drawer and allows the user to manage connected devices.
 * It implements the [MenuOption] interface, which defines the required properties for dynamic menu entries.
 *
 * @constructor This is a singleton object; it is not instantiated directly.
 *
 * @see MenuOption for the base interface.
 */

object HomeMenuOption : MenuOption {
    override val icon = Icons.Default.Home
    override val route = "home"
    override val titleResId = R.string.home_title

    @Composable
    override fun Content() {
        HomeScreen()
    }
}