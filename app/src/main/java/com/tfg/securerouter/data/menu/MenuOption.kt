package com.tfg.securerouter.data.app.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface representing a menu option in the navigation drawer.
 *
 * Each implementation of this interface defines:
 * - Its visual appearance in the drawer (icon and title).
 * - Its navigation route.
 * - The composable screen content to render when selected.
 *
 * This abstraction allows the app to dynamically render and navigate between different
 * sections from a centralized list of [MenuOption]s.
 *
 * @property icon The [ImageVector] icon shown in the drawer.
 * @property route The unique navigation route [String] used for routing.
 * @property titleResId The [String] resource ID for the screen title (supports localization).
 *
 * @see androidx.navigation.NavController for handling navigation using routes.
 */

interface MenuOption {
    val icon: ImageVector
    val route: String
    val titleResId: Int

    //The composable screen to be shown when this menu item is selected.
    @Composable fun Content()
}


