package com.tfg.securerouter.ui.app.screens.devices_options

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.devices_options.DevicesOptionsCoordinator
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceTimesRuleModel
import com.tfg.securerouter.data.json.jsons.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.utils.AppSession
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

        if (macArg.isNullOrBlank()) {
            setComponents({ M3Text("Dispositivo no especificado.") })
            RenderScreen()
            return
        }

        val routerId = AppSession.routerId
        if (routerId == null) {
            setComponents({ M3Text("No hay router seleccionado.") })
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

        RenderScreen()
    }
}
