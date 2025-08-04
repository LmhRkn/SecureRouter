package com.tfg.securerouter.data.json.language

import com.tfg.securerouter.data.json.BaseCache
import kotlinx.serialization.*

@Serializable
data class LanguageCache(
    val data: MutableMap<String, String> = mutableMapOf()
)

object LanguageManagerCache : BaseCache<LanguageCache>(
    LanguageCache.serializer(),
    "language_cache.json"
) {
    override fun createEmptyCache() = LanguageCache()

    override fun getDataMap(): MutableMap<String, String> = cacheData!!.data
}
