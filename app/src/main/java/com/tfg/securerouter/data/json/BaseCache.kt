package com.tfg.securerouter.data.json

import android.content.Context
import android.util.Log
import com.tfg.securerouter.data.json.jsons.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.json.jsons.language.LanguageManagerCache
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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

    protected abstract fun getDataMap(): MutableMap<String, String>

    open fun get(key: String): String? {
        return getDataMap()[key.uppercase()]
    }

    open fun put(key: String, value: String) {
        getDataMap()[key.uppercase()] = value
        saveToDisk()
    }

    fun flush() {
        saveToDisk()
    }

    fun clearCache() {
        getDataMap().clear()
        if (this::cacheFile.isInitialized && cacheFile.exists()) {
            cacheFile.delete()
        }
        cacheData = createEmptyCache()
    }

    protected fun persistCache() {
        saveToDisk()
    }

    private fun saveToDisk() {
        cacheData?.let {
            cacheFile.writeText(Json.encodeToString(serializer, it))
        }
    }

    fun dumpRaw(): String =
        cacheData?.let { Json.encodeToString(serializer, it) } ?: "{}"

    fun dumpPretty(): String =
        cacheData?.let { Json { prettyPrint = true }.encodeToString(serializer, it) } ?: "{}"

    fun logDump(tag: String = "CacheDump") {
        Log.d(tag, dumpPretty())
    }
}