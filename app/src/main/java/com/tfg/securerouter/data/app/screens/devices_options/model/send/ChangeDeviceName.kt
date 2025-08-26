package com.tfg.securerouter.data.app.screens.devices_options.model.send

import com.tfg.securerouter.data.json.jsons.device_manager.DeviceManagerCache


object ChangeDeviceName {
    fun updateDeviceAlias(mac: String, newAlias: String) {
        val deviceData = DeviceManagerCache.getDevice(mac)
        val updateDevice = deviceData?.copy(
            hostname = newAlias
        )

        if (updateDevice == null) return
        DeviceManagerCache.put(updateDevice)
    }
}
