package com.tfg.securerouter.data.menu.menu_screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.runtime.Composable
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.ui.app.screens.device_manager.DeviceManagerScreen

/**
 * Object representing the "Administrar Dispositivos" menu option.
 *
 * This screen is part of the main navigation drawer and allows the user to manage connected devices.
 * It implements the [MenuOption] interface, which defines the required properties for dynamic menu entries.
 *
 * @constructor This is a singleton object; it is not instantiated directly.
 *
 * @see MenuOption for the base interface.
 */

object AdministrarDispositivosMenuOption : MenuOption {
    override val icon = Icons.Filled.DesktopWindows
    override val route = "administrar"
    override val titleResId = R.string.administrar_title

    @Composable
    override fun Content() {
        val adminatrar = DeviceManagerScreen()
        adminatrar.AdministrarScreenInit()
    }
}