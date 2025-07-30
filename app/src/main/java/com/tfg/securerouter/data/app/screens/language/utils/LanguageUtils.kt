package com.tfg.securerouter.data.app.screens.language.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache.put
import java.util.Locale
import com.tfg.securerouter.data.json.language.LanguageManagerCache

fun saveDeviceLanguage(context: Context, languageCode: String? = null) {
    println("LanguageCode: $languageCode")
    val codeToSave = languageCode ?: run {
        val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            context.resources.configuration.locale
        }
        locale.language
    }
    println("codeToSave: $codeToSave")

    put("DEVICE_LANGUAGE", codeToSave)
}


fun setDeviceLanguage(context: Context) {
    val cachedLanguage = LanguageManagerCache.get("DEVICE_LANGUAGE")
    saveDeviceLanguage(context, cachedLanguage)
}
