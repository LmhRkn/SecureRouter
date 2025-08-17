package com.tfg.securerouter.ui.app.screens.devices_options

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.devices_options.DevicesOptionsCoordinator
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceFilterWebRuleModel
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceTimesRuleModel
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiRouterInfoModel
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsBlockButton
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsBlocked
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsData
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsFilters
import com.tfg.securerouter.ui.app.screens.devices_options.components.DeviceOptionsTimes

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
        val mac = navController.currentBackStackEntry?.arguments?.getString("mac")!!

        val devicesOptionsCoordinator = coordinator as DevicesOptionsCoordinator
        val deviceTimesRule     = devicesOptionsCoordinator.modules.filterIsInstance<DeviceTimesRuleModel>().first().state.collectAsState().value
        val deviceFilterWebRule = devicesOptionsCoordinator.modules.filterIsInstance<DeviceFilterWebRuleModel>().first().state.collectAsState().value

        // ← estado elevado a la pantalla
        var device by remember(mac) { mutableStateOf(DeviceManagerCache.getDevice(mac)!!) }

        // (opcional) si quieres evitar duplicar componentes en recomposiciones,
        // añade en ScreenDefault una función clearComponents() y llámala aquí:
        // clearComponents()

        addComponents(
            { DeviceOptionsData(mac, {}) },
            { DeviceOptionsTimes(deviceTimesRule = deviceTimesRule, mac = mac) },
            { DeviceOptionsFilters(deviceFilterWebRule = deviceFilterWebRule, mac = mac) },
            {
                DeviceOptionsBlocked(device)
                Spacer(Modifier.height(8.dp))
                DeviceOptionsBlockButton(
                    deviceModel = device,
                    onChanged = { updated -> device = updated}
                )
            }
        )

        RenderScreen()
    }
}
