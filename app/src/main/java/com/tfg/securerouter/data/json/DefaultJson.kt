package com.tfg.securerouter.data.json

import android.content.Context
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

/**
 * Abstract base class for JSON-based caching mechanisms.
 *
 * Usage:
 * This class provides a generic implementation for managing cached data stored as JSON files.
 * Subclasses must define how to create an empty cache and provide access to the internal data map.
 * It supports operations such as initialization, retrieval, updating, clearing, and persisting data.
 *
 * @param T The type of the cache data, annotated with [Serializable] for JSON encoding/decoding.
 * @param serializer A [KSerializer] for serializing and deserializing the cache data.
 * @param fileName The name of the JSON file where the cache is stored.
 *
 * @property cacheData The in-memory cache data.
 * @property cacheFile The file where the cache data is stored.
 */
abstract class BaseCache<T : Any>(
    private val serializer: KSerializer<T>,
    private val fileName: String
) {
    protected var cacheData: T? = null
    private lateinit var cacheFile: File

    /**
     * Initializes the cache system by loading data from disk if available.
     * If the cache file does not exist, it creates an empty cache instance using [createEmptyCache].
     *
     * @param context The Android [Context] used to locate the internal files directory.
     */
    fun init(context: Context) {
        if (!this::cacheFile.isInitialized) {
            cacheFile = File(context.filesDir, fileName)
            cacheData = if (cacheFile.exists()) {
                Json.decodeFromString(serializer, cacheFile.readText())
            } else {
                createEmptyCache()
            }
        }
    }

    /**
     * Default implementation of a simple key-value cache.
     * Provides a fallback data structure for caches that only need a string-to-string mapping.
     *
     * @property data A mutable map holding string keys and values.
     */
    @Serializable
    data class DefaultCache(val data: MutableMap<String, String> = mutableMapOf())

    /**
     * Creates and returns an empty cache instance of type [T].
     * Subclasses must override this method to define how an empty cache is constructed.
     *
     * @return A new instance of [T] representing an empty cache.
     */
    abstract fun createEmptyCache(): T

    /**
     * Retrieves a value from the cache by its key.
     * The key lookup is case-insensitive (converted to uppercase).
     *
     * @param key The key to search for in the cache.
     * @return The cached value associated with the key, or null if not found.
     */
    fun get(key: String): String? {
        return getDataMap()[key.uppercase()]
    }

    /**
     * Stores a key-value pair in the cache.
     * The key is converted to uppercase to enforce case-insensitive storage.
     * Automatically persists the change to disk.
     *
     * @param key The key under which to store the value.
     * @param value The value to store in the cache.
     */
    fun put(key: String, value: String) {
        getDataMap()[key.uppercase()] = value
        saveToDisk()
    }

    /**
     * Persists the current in-memory cache data to disk.
     * Should be called when the app terminates or after important updates.
     */
    fun flush() {
        saveToDisk()
    }

    /**
     * Clears all entries in the cache and persists the empty state to disk.
     */
    fun clearCache() {
        getDataMap().clear()
        saveToDisk()
    }

    /**
     * Provides access to the internal data map of the cache.
     * Subclasses must override this to return their specific mutable map representation.
     *
     * @return A mutable map containing the cache's key-value pairs.
     */
    protected abstract fun getDataMap(): MutableMap<String, String>

    /**
     * Encodes the current in-memory cache data as JSON and writes it to the cache file on disk.
     * Called automatically by [put], [clearCache], and [flush].
     */
    private fun saveToDisk() {
        cacheData?.let {
            cacheFile.writeText(Json.encodeToString(serializer, it))
        }
    }
}
