package com.tfg.securerouter.data.json

import android.content.Context
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache

/**
 * Centralized initializer for all JSON-based cache instances.
 *
 * Usage:
 * This object initializes and persists all cache objects extending [BaseCache].
 * It provides a single entry point to load cached data from disk at app startup,
 * and to flush changes back to disk during app termination or when needed.
 *
 * @property caches A [List] of cache instances (singletons) implementing [BaseCache].
 * Each cache in the list will be initialized and flushed automatically.
 *
 * @see BaseCache
 * @see JsonRegistry
 */
object JsonInitializer {

    /**
     * List of all cache instances that need to be initialized and flushed.
     *
     * IMPORTANT:
     * Any new cache class extending [BaseCache] must be added to this list.
     * Otherwise, it will not be loaded into memory or persisted to disk.
     */
    private val caches = JsonRegistry.caches

    /**
    * Initializes all caches by reading their data from disk into memory.
    *
    * @param context The Android [Context] required for cache initialization.
    */
    fun init(context: Context) {
        caches.forEach { it.init(context) }
    }

    /**
    * Persists all in-memory cache data back to disk.
    */
    fun flush() {
        caches.forEach { it.flush() }
    }

    /**
     * Clears all in-memory cache data.
     */
    fun clearCache() {
        caches.forEach { it.clearCache() }
    }
}
