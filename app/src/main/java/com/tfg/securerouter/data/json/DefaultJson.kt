package com.tfg.securerouter.data.json

import android.content.Context
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

abstract class BaseCache<T : Any>(
    private val serializer: KSerializer<T>,
    private val fileName: String
) {
    protected var cacheData: T? = null
    private lateinit var cacheFile: File

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

    @Serializable
    data class DefaultCache(val data: MutableMap<String, String> = mutableMapOf())

    abstract fun createEmptyCache(): T

    fun get(key: String): String? {
        return getDataMap()[key.uppercase()]
    }

    fun put(key: String, value: String) {
        getDataMap()[key.uppercase()] = value
        saveToDisk()
    }

    fun flush() {
        saveToDisk()
    }

    fun clearCache() {
        getDataMap().clear()
        saveToDisk()
    }

    protected abstract fun getDataMap(): MutableMap<String, String>

    private fun saveToDisk() {
        cacheData?.let {
            cacheFile.writeText(Json.encodeToString(serializer, it))
        }
    }
}
