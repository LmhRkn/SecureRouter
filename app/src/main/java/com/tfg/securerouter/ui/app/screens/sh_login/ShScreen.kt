package com.tfg.securerouter.ui.app.screens.sh_login

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.language.LanguageCoordinator
import com.tfg.securerouter.data.app.screens.router_selector.RouterSelectorCoordinator
import com.tfg.securerouter.data.app.screens.sh_login.ShLoginCoordinator
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.language.components.LanguageButtons
import com.tfg.securerouter.ui.app.screens.language.components.LanguageList
import com.tfg.securerouter.ui.app.screens.sh_login.components.ShLogin

class ShScreen: ScreenDefault() {

    @Composable
    @Override
    fun ShLoginScreenInit() {
        val coordinator: ShLoginCoordinator = viewModel()

        ScreenInit(coordinator)
    }


    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val shLoginCoordinator = coordinator as? ShLoginCoordinator
            ?: throw IllegalArgumentException("Expected ShLoginCoordinator")

        val navController = LocalNavController.current


        setComponents(
            {
                ShLogin(navController)
            },
        )

        RenderScreen()
    }
}
