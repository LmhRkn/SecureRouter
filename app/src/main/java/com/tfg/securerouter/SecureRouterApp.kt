package com.tfg.securerouter

import android.app.Application
import com.tfg.securerouter.data.json.JsonInitializer
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache

class SecureRouterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        JsonInitializer.init(this)
//        JsonInitializer.clearCache()
    }

    override fun onTerminate() {
        JsonInitializer.flush()
        super.onTerminate()
    }
}
