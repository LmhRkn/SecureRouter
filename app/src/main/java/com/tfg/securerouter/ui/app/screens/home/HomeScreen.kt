package com.tfg.securerouter.ui.app.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.home.HomeCoordinator
import com.tfg.securerouter.data.app.screens.home.model.load.ConnectedDeviceModel
import com.tfg.securerouter.data.app.screens.home.model.load.HomeRouterInfoModel
import com.tfg.securerouter.data.app.screens.home.model.send.SendRouterName
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.home.components.ConnectedDevicesList
import com.tfg.securerouter.ui.app.screens.home.components.HomeRouterInfoSection
import com.tfg.securerouter.ui.notice.alerts.AlertModal
import com.tfg.securerouter.ui.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.notice.tutorials.TutorialModal
import com.tfg.securerouter.data.app.screens.home.tutorials.RegisterHomeTutorial
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiTrafficGraphModel
import com.tfg.securerouter.data.utils.PromptHost
import com.tfg.securerouter.ui.notice.tutorials.AutoOpenTutorialOnce

class HomeScreen : ScreenDefault() {

    @Composable
    fun HomeScreenInit() {
        val coordinator: HomeCoordinator = viewModel()
        LaunchedEffect(Unit) {
            AppSession.routerSelected = true
        }
        ScreenInit(coordinator)
    }

    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val homeCoordinator = coordinator as? HomeCoordinator
            ?: throw IllegalArgumentException("Expected HomeCoordinator")

        AutoOpenTutorialOnce(
            routerId = AppSession.routerId,
            screenKey = HomeMenuOption.route
        )

        val routerInfoModel = homeCoordinator.modules.filterIsInstance<HomeRouterInfoModel>().first()
        val connectedDevicesModel = homeCoordinator.modules.filterIsInstance<ConnectedDeviceModel>().first()

        val routerState = routerInfoModel.state.collectAsState().value
        val devicesState = connectedDevicesModel.state.collectAsState().value

        var showAlert by rememberSaveable { mutableStateOf(false) }
        var pendingAlias by rememberSaveable { mutableStateOf<String?>(null) }



        RegisterHomeTutorial()

        val tutorialSpec by TutorialCenter.spec.collectAsState()
        val tutorialOpen by TutorialCenter.open.collectAsState()

        setComponents(
            {
                HomeRouterInfoSection(
                    state = routerState,
                    onEditAliasClick = { newAlias ->
                        pendingAlias  = newAlias
                        showAlert = true
                    }
                )
            },
            {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Router,
                        contentDescription = "Router icon",
                        modifier = Modifier.size(128.dp)
                    )
                }
            },
            { ConnectedDevicesList(devicesState = devicesState, weight = 0.4f) },
        )

        val alert = remember {
            AlertSpec(
                title = "¿Aplicar cambios?",
                message = "Esta acción reiniciará el router. ¿Continuar?",
                confirmText = "Aceptar",
                cancelText = "Cancelar",
                showCancel = true
            )
        }

        Box(Modifier.fillMaxSize()) {
            RenderScreen()

            if (tutorialOpen && tutorialSpec != null) {
                TutorialModal(
                    spec = tutorialSpec!!,
                    onSkip = { TutorialCenter.close() },
                    onFinish = { TutorialCenter.close() }
                )
            }

            if (showAlert) {
                AlertModal(
                    spec = alert,
                    onConfirm = {
                        val alias = pendingAlias?.trim().orEmpty()
                        if (alias.isNotEmpty()) {
                            RouterSelectorCache.update(AppSession.routerId.toString()) { r -> r.copy(name = alias) }
                            SendRouterName.updateRouterAlias(routerState.wirelessName, alias)
                        }
                        showAlert = false
                        pendingAlias = null
                    },
                    onCancel = {
                        showAlert = false
                        pendingAlias = null
                    }
                )
            }
        }
    }
}