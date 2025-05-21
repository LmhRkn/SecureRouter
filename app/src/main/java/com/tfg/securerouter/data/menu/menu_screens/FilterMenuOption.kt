package com.tfg.securerouter.data.app.menu.menu_screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.ui.icons.FilterIcon
import com.tfg.securerouter.ui.icons.WifiIcon

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
    override val icon = FilterIcon
    override val route = "filter"
    override val titleResId = R.string.filter_title

    @Composable
    override fun Content() {
        Text("Filter Hola", color = Color.Black)
    }
}