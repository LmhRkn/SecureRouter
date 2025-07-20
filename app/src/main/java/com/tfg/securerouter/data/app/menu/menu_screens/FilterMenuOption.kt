package com.tfg.securerouter.data.app.menu.menu_screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.ui.app.screens.filter.FilterScreen

/**
 * Object representing the "Filter" menu option.
 *
 * This screen is part of the main navigation drawer and allows the user to manage connected devices.
 * It implements the [MenuOption] interface, which defines the required properties for dynamic menu entries.
 *
 * @constructor This is a singleton object; it is not instantiated directly.
 *
 * @see MenuOption for the base interface.
 */

object FilterMenuOption : MenuOption {
    override val icon = Icons.Filled.FilterAlt
    override val route = "filter"
    override val titleResId = R.string.filter_title

    @Composable
    override fun Content() {
        val filter = FilterScreen()
        filter.FilterScreenInit()
    }
}