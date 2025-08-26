package com.tfg.securerouter.data.json.registry

import com.tfg.securerouter.data.json.jsons.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.json.jsons.language.LanguageManagerCache
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache

object JsonRegistry {
    val caches = listOf(
        DeviceManagerCache,
        LanguageManagerCache,
        RouterSelectorCache,
    )
}