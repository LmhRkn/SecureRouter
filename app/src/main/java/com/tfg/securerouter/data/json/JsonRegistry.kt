package com.tfg.securerouter.data.json

import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.json.language.LanguageManagerCache
import com.tfg.securerouter.data.json.router_selector.RouterSelctorCache

object JsonRegistry {
    val caches = listOf(
        DeviceManagerCache,
        LanguageManagerCache,
        RouterSelctorCache,
    )
}