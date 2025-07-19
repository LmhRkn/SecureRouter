package com.tfg.securerouter.data.json.device_manager

import android.content.Context
import com.tfg.securerouter.data.json.BaseCache
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class DeviceCache (val data: MutableMap<String, String> = mutableMapOf())

object DeviceManagerCache  : BaseCache<DeviceCache>(
    DeviceCache.serializer(),
    "device_cache.json"
) {
    override fun createEmptyCache() = DeviceCache()
    override fun getDataMap() = cacheData!!.data
}
