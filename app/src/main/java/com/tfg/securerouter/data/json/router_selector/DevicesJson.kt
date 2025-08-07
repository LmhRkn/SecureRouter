package com.tfg.securerouter.data.json.router_selector

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.json.BaseCache
import kotlinx.serialization.*
import com.tfg.securerouter.data.app.common.screen_components.devices.model.SerializableDeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toDeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toSerializable
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import kotlinx.serialization.json.Json

@Serializable
data class RouterCache(
    val data: MutableMap<String, String> = mutableMapOf()  // JSON strings por MAC
)

object RouterSelctorCache : BaseCache<RouterCache>(
    RouterCache.serializer(),
    "device_cache.json"
) {
    override fun createEmptyCache() = RouterCache()

    override fun getDataMap(): MutableMap<String, String> = cacheData!!.data

    fun getRouter(id: String): RouterInfo? {
        return getDataMap()[id]?.let {
            try {
                Json.decodeFromString<RouterInfo>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getRouters(): List<RouterInfo> {
        return getDataMap().values.mapNotNull { jsonString ->
            try {
                Json.decodeFromString<RouterInfo>(jsonString)
            } catch (e: Exception) {
                null
            }
        }
    }


    fun put(router: RouterInfo) {
        val json = Json.encodeToString(router)
        put(router.id.toString(), json)
    }

    fun update(id: String, update: (RouterInfo) -> RouterInfo) {
        val current = getRouter(id)
        if (current != null) {
            put(update(current))
        }
    }
}
