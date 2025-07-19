package com.tfg.securerouter.data.json.device_manager

import com.tfg.securerouter.data.json.BaseCache
import kotlinx.serialization.*

/**
 * Serializable data class representing the structure of the device manager cache.
 *
 * Usage:
 * This class holds a mutable map of key-value pairs where both keys and values are strings.
 * It provides the data container for [DeviceManagerCache] and supports JSON serialization.
 *
 * @property data A mutable map storing the cached device information as string key-value pairs.
 */
@Serializable
data class DeviceCache(
    val data: MutableMap<String, String> = mutableMapOf()
)

/**
 * Singleton object for managing device-related cached data.
 *
 * Usage:
 * This object extends [BaseCache] to provide persistence and in-memory caching of device data.
 * It initializes with an empty [DeviceCache] if no existing cache file is found.
 *
 * File:
 * The cache data is stored in a JSON file named `device_cache.json` within the app's internal storage.
 *
 * @see BaseCache
 */
object DeviceManagerCache : BaseCache<DeviceCache>(
    DeviceCache.serializer(),
    "device_cache.json"
) {
    /**
     * Creates and returns a new empty [DeviceCache] instance.
     * Called when no existing cache file is found on initialization.
     *
     * @return A new empty [DeviceCache].
     */
    override fun createEmptyCache() = DeviceCache()

    /**
     * Provides access to the internal data map of the cache.
     *
     * @return The mutable map of key-value pairs from the current [DeviceCache].
     */
    override fun getDataMap() = cacheData!!.data
}
