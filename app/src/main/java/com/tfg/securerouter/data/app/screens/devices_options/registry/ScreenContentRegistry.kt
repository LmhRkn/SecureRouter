package com.tfg.securerouter.data.app.screens.devices_options.registry

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceFilterWebRuleModel
import com.tfg.securerouter.data.app.screens.devices_options.model.load.DeviceTimesRuleModel


class DevicesOptionsScreenContentRegistry(sharedCache: MutableMap<String, Any>) {
    val modules: List<ScreenComponentModelDefault> = listOf(
        DeviceTimesRuleModel(sharedCache),
        DeviceFilterWebRuleModel(sharedCache)
    )
}