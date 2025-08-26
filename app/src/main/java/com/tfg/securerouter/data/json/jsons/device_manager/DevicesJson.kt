package com.tfg.securerouter.data.json.jsons.device_manager

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.SerializableDeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toDeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toSerializable
import com.tfg.securerouter.data.json.RouterScopedBaseCache
import com.tfg.securerouter.data.json.RouterScopedEnvelope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object DeviceManagerCache : RouterScopedBaseCache(
    RouterScopedEnvelope.serializer(),
    "device_cache.json"
) {
    fun getDevice(mac: String): DeviceModel? {
        return getDataMap()[mac.uppercase()]?.let {
            try {
                Json.decodeFromString<SerializableDeviceModel>(it).toDeviceModel()
            } catch (_: Exception) {
                null
            }
        }
    }

    fun put(device: DeviceModel) {
        val json = Json.encodeToString(device.toSerializable())
        put(device.mac, json)
    }

    fun update(mac: String, update: (DeviceModel) -> DeviceModel) {
        getDevice(mac)?.let { put(update(it)) }
    }

    fun getAllDevices(): List<DeviceModel> {
        return getDataMap().values.mapNotNull { json ->
            try {
                Json.decodeFromString<SerializableDeviceModel>(json).toDeviceModel()
            } catch (_: Exception) {
                null
            }
        }
    }
}
