package com.tfg.securerouter.data.json.router_selector

import com.tfg.securerouter.data.json.BaseCache
import kotlinx.serialization.*
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import kotlinx.serialization.json.Json

@Serializable
data class RouterCache(
    val data: MutableMap<String, String> = mutableMapOf()
)

object RouterSelectorCache : BaseCache<RouterCache>(
    RouterCache.serializer(),
    "router_cache.json"
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

    fun nextId(): Int = (getRouters().maxOfOrNull { it.id } ?: 0) + 1
}
