package com.tfg.securerouter.ui.app.screens.router_selector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.notice.model.tutorials.AutoOpenTutorialOnce
import com.tfg.securerouter.data.app.notice.utils.PromptHost
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.home.tutorials.RegisterRotuerSelectorTutorial
import com.tfg.securerouter.data.app.screens.router_selector.RouterSelectorCoordinator
import com.tfg.securerouter.data.app.screens.wifi.tutorials.WifiTutorial
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialModal
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
            AppSession.createSSHPassword = null
        }

        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val routerCoordinator = coordinator as RouterSelectorCoordinator
        val navController = LocalNavController.current
        val tutorialSpec by TutorialCenter.spec.collectAsState()
        val tutorialOpen by TutorialCenter.open.collectAsState()

        PromptHost()
        AutoOpenTutorialOnce(
            routerId = AppSession.routerId,
            screenKey = HomeMenuOption.route
        )

        RegisterRotuerSelectorTutorial()
        setComponents(
            { RoutersList(navController) }
        )

        Box(Modifier.fillMaxSize()) {
            RenderScreen()

            if (tutorialOpen && tutorialSpec != null) {
                TutorialModal(
                    spec = tutorialSpec!!,
                    onSkip = { TutorialCenter.close() },
                    onFinish = { TutorialCenter.close() }
                )
            }
        }
    }
}
