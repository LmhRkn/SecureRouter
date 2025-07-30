package com.tfg.securerouter

import android.app.Application
import com.tfg.securerouter.data.app.screens.language.utils.setDeviceLanguage
import com.tfg.securerouter.data.json.JsonInitializer

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

        JsonInitializer.init(this)
        setDeviceLanguage(this)

        // Used to clear the cache during development or testing.
        // It should be commented out or removed in production builds.
//        JsonInitializer.clearCache() // Clears all cached data.
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
