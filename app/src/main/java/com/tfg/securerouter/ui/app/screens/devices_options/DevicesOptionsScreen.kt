package com.tfg.securerouter.ui.app.screens.devices_options

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.notice.model.tutorials.AutoOpenTutorialOnce
import com.tfg.securerouter.data.app.notice.utils.PromptHost
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.devices_options.DevicesOptionsCoordinator
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceTimesRuleModel
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.RegisterDevicesOptionTutorial
import com.tfg.securerouter.data.app.screens.wifi.tutorials.WifiTutorial
import com.tfg.securerouter.data.json.jsons.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialModal
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsBlockButton
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsBlocked
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsData
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsTimes
import androidx.compose.material3.Text as M3Text

class DevicesOptionsScreen: ScreenDefault() {
    @Composable
    @Override
    fun DevicesOptionsScreenInit() {
        val coordinator: DevicesOptionsCoordinator = viewModel()

        ScreenInit(coordinator)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun ScreenContent(coordinator: ScreenCoordinatorDefault) {
        val navController = LocalNavController.current
        val macArg = navController.currentBackStackEntry?.arguments?.getString("mac")?.uppercase()

        PromptHost()
        AutoOpenTutorialOnce(
            routerId = AppSession.routerId,
            screenKey = HomeMenuOption.route
        )

        if (macArg.isNullOrBlank()) {
            setComponents({ M3Text(stringResource(R.string.device_option_unspecified_device_error)) })
            RenderScreen()
            return
        }

        val routerId = AppSession.routerId
        if (routerId == null) {
            setComponents({ M3Text(stringResource(R.string.device_option_no_router_selected_error)) })
            RenderScreen()
            return
        }

        val initial = DeviceManagerCache.getDevice(macArg)
        if (initial == null) {
            setComponents({ M3Text("Dispositivo $macArg no encontrado en el router $routerId.") })
            RenderScreen()
            return
        }

        var deviceModel by remember(macArg) { mutableStateOf(initial) }

        val devicesOptionsCoordinator = coordinator as? DevicesOptionsCoordinator
            ?: throw IllegalArgumentException("ExpectedDevicesOptionsCoordinator")

        val deviceTimesRuleModel = devicesOptionsCoordinator.modules
            .filterIsInstance<DeviceTimesRuleModel>().first()
        val deviceTimesRule = deviceTimesRuleModel.state.collectAsState().value
        val tutorialSpec by TutorialCenter.spec.collectAsState()
        val tutorialOpen by TutorialCenter.open.collectAsState()

        RegisterDevicesOptionTutorial()

        setComponents(
            { DeviceOptionsData(macArg, {}) },
            { DeviceOptionsTimes(deviceTimesRule, macArg) },
            {
                DeviceOptionsBlocked(deviceModel)
                Spacer(Modifier.height(8.dp))
                DeviceOptionsBlockButton(
                    deviceModel = deviceModel,
                    onChanged = { updated -> deviceModel = updated }
                )
            }
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
