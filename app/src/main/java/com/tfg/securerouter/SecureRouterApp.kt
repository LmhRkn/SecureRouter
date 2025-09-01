package com.tfg.securerouter

import android.app.Application
import android.util.Log
import com.tfg.securerouter.data.app.screens.language.utils.setDeviceLanguage
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.json.JsonInitializer
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import kotlin.system.exitProcess

/**
 * Custom [Application] class for SecureRouter.
 *
 * Responsibilities:
 * - Initializes all JSON-based cache instances at app startup via [JsonInitializer].
 * - Flushes cached data to disk when the app terminates.
 * - Provides a central place to configure global app-wide dependencies.
 *
 * @see JsonInitializer For initializing and persisting [BaseCache] instances.
 */
class SecureRouterApp : Application() {
    /**
     * Called when the application is starting, before any activity or service is created.
     *
     * Initializes all cache instances so they are ready for use across the app.
     */
    override fun onCreate() {
        super.onCreate()
        ContextProvider.init(this)
        JsonInitializer.init(this)
        setDeviceLanguage(this)


//        RouterSelectorCache.update("1") { r -> r.copy(mac = "AA:BB:CC:DD:EE:FF") }
//        RouterSelectorCache.update("1") { r -> r.copy(sshPassword = "12345678") }
//        Log.d("ROUTERS", "routers: ${RouterSelectorCache.getRouters()}")

/////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////// DESCOMENTAR ESTO Y EJECUTAR Y VOLVER A COMENTAR ////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////
//        RouterSelectorCache.put(
//            RouterInfo(
//                name = "SecureRouter5G",
//                localIp = "192.168.10.1",
//                mac = "AA:BB:CC:DD:EE:FF",
//                id = 1,
//                isVpn = false,
//                sshPassword = "12345678"
//            )
//        )
//------------------------------------------------------------------------------------------------

        // Used to clear the cache during development or testing.
        // It should be commented out or removed in production builds.
//        JsonInitializer.clearCache() // Clears all cached data.
//        exitProcess(0)
    }

    /**
     * Called when the system is about to terminate the application.
     *
     * Flushes in-memory cache data back to disk to ensure persistence.
     */
    override fun onTerminate() {
        JsonInitializer.flush()
        super.onTerminate()
    }
}
