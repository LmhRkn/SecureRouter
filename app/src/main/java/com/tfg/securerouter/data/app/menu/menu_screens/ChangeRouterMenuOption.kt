package com.tfg.securerouter.data.app.menu.menu_screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.MenuOption
import com.tfg.securerouter.ui.app.screens.router_selector.RouterSelectionScreen
import com.tfg.securerouter.ui.app.screens.wifi.WifiScreen

object RouterSelectorMenuOption : MenuOption {
    override val icon = Icons.Filled.Router
    override val route = "router_selection"
    override val titleResId = R.string.change_router_title

    @Composable
    override fun Content() {
        val changeRouter = RouterSelectionScreen()
        changeRouter.RouterSelectionScreenInit()
    }
}