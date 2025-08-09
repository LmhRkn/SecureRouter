package com.tfg.securerouter.ui.app.screens.devices_options

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.devices_options.DevicesOptionsCoordinator
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceTimesRuleModel
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiRouterInfoModel
import com.tfg.securerouter.ui.app.screens.ScreenDefault
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
        val mac = navController.currentBackStackEntry?.arguments?.getString("mac")

        val devicesOptionsCoordinator = coordinator as? DevicesOptionsCoordinator
            ?: throw IllegalArgumentException("ExpectedDevicesOptionsCoordinator")

        val deviceTimesRuleModel = devicesOptionsCoordinator.modules
            .filterIsInstance<DeviceTimesRuleModel>()
            .first()

        val deviceTimesRule = deviceTimesRuleModel.state.collectAsState().value

        addComponents(
            { DeviceOptionsData(mac!!, {}) },
            { DeviceOptionsTimes(deviceTimesRule, mac!!) },
            { DeviceOptionsFilters()},
        )

        RenderScreen()
    }
}
