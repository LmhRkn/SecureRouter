package com.tfg.securerouter.ui.app.screens.router_selector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.router_selector.RouterSelectorCoordinator
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.router_selector.components.RoutersList


class RouterSelectionScreen : ScreenDefault() {

    @Composable
    @Override
    fun RouterSelectionScreenInit() {
        val coordinator: RouterSelectorCoordinator = viewModel()
        LaunchedEffect(Unit) {
            AppSession.routerSelected = false
            AppSession.routerId = null
            AppSession.routerIp = null
        }

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val routerCoordinator = coordinator as RouterSelectorCoordinator
        val navController = LocalNavController.current

        setComponents(
            { RoutersList(navController) }
        )

        RenderScreen()
    }
}
