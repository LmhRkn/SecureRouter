package com.tfg.securerouter.data.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


interface ScreenOptions {
    val route: String

    //The composable screen to be shown when this menu item is selected.
    @Composable fun Content()
}


