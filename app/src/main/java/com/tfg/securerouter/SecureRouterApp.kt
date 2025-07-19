package com.tfg.securerouter

import android.app.Application
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache

class SecureRouterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DeviceManagerCache.init(this)
//        DeviceCache.clearCache()
    }

    override fun onTerminate() {
        DeviceManagerCache.flush()
        super.onTerminate()
    }
}
