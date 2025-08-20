package com.tfg.securerouter.data.json

import com.tfg.securerouter.data.utils.AppSession
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class RouterScopedEnvelope(
    val data: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
)

open class RouterScopedBaseCache(
    serializer: KSerializer<RouterScopedEnvelope>,
    fileName: String
) : BaseCache<RouterScopedEnvelope>(serializer, fileName) {

    override fun createEmptyCache() = RouterScopedEnvelope()

    override fun getDataMap(): MutableMap<String, String> {
        val rid = AppSession.routerId?.toString() ?: "GLOBAL"
        val env = (cacheData ?: createEmptyCache().also { cacheData = it })
        return env.data.getOrPut(rid) { mutableMapOf() }
    }

    override fun get(key: String): String? {
        val rid = AppSession.routerId?.toString() ?: "GLOBAL"
        val env = cacheData ?: return null
        return env.data[rid]?.get(key.uppercase())
    }

    override fun put(key: String, value: String) {
        val rid = AppSession.routerId?.toString() ?: "GLOBAL"
        val env = (cacheData ?: createEmptyCache().also { cacheData = it })
        val bucket = env.data.getOrPut(rid) { mutableMapOf() }
        bucket[key.uppercase()] = value
        persistCache()
    }

    fun dumpBucketPretty(routerId: Int? = AppSession.routerId): String {
        val rid = routerId?.toString() ?: "GLOBAL"
        val bucket = cacheData?.data?.get(rid).orEmpty()
        return Json { prettyPrint = true }.encodeToString(bucket)
    }
}
