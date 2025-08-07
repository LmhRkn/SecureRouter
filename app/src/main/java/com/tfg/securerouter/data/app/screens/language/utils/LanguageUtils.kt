package com.tfg.securerouter.data.app.screens.language.utils

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale
import com.tfg.securerouter.data.json.language.LanguageManagerCache

fun saveDeviceLanguage(context: Context, languageCode: String? = null): String {
    val codeToSave = when {
        !languageCode.isNullOrBlank() -> languageCode
        LanguageManagerCache.get("DEVICE_LANGUAGE") != null -> LanguageManagerCache.get("DEVICE_LANGUAGE")!!
        else -> {
            val locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.resources.configuration.locales[0]
            } else {
                context.resources.configuration.locale
            }
            locale.language
        }
    }

    LanguageManagerCache.put("DEVICE_LANGUAGE", codeToSave)
    return codeToSave
}

fun setDeviceLanguage(context: Context, languageCode: String? = null) {

    var finalLanguage = languageCode ?: LanguageManagerCache.get("DEVICE_LANGUAGE")
    finalLanguage = saveDeviceLanguage(context, finalLanguage)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val localeManager = context.getSystemService(LocaleManager::class.java)
        localeManager.applicationLocales = LocaleList.forLanguageTags(finalLanguage)
    } else {
        val localeList = LocaleListCompat.forLanguageTags(finalLanguage)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
