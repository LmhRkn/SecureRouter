package com.tfg.securerouter.data.app.screens.other_screen

import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.menu.ScreenOptions
import com.tfg.securerouter.data.app.screens.router_selector.registry.RouterSelectorScreenContentRegistry
import com.tfg.securerouter.ui.app.screens.language.LanguageScreen
import com.tfg.securerouter.ui.app.screens.router_selector.RouterSelectionScreen

object RouterSelectorScreenOption : ScreenOptions {
    override val route = "router_selection"

    @Composable
    override fun Content() {
        val router = RouterSelectionScreen()
        router.RouterSelectionScreenInit()
    }
}