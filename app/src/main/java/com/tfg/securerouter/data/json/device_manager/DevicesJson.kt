package com.tfg.securerouter.data.json.device_manager

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.json.BaseCache
import kotlinx.serialization.*
import com.tfg.securerouter.data.app.common.screen_components.devices.model.SerializableDeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toDeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toSerializable
import kotlinx.serialization.json.Json

@Serializable
data class DeviceCache(
    val data: MutableMap<String, String> = mutableMapOf()  // JSON strings por MAC
)

object DeviceManagerCache : BaseCache<DeviceCache>(
    DeviceCache.serializer(),
    "device_cache.json"
) {
    override fun createEmptyCache() = DeviceCache()

    override fun getDataMap(): MutableMap<String, String> = cacheData!!.data

    fun getDevice(mac: String): DeviceModel? {
        return getDataMap()[mac.uppercase()]?.let {
            try {
                Json.decodeFromString<SerializableDeviceModel>(it).toDeviceModel()
            } catch (e: Exception) {
                null
            }
        }
    }

    fun put(device: DeviceModel) {
        val json = Json.encodeToString(device.toSerializable())
        put(device.mac, json)
    }

    fun update(mac: String, update: (DeviceModel) -> DeviceModel) {
        val current = getDevice(mac)
        if (current != null) {
            put(update(current))
        }
    }
}
