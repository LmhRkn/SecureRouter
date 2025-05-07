package com.tfg.securerouter.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun applyAppLocale(context: Context, langCode: String): Context {
    val locale = Locale(langCode)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    return context.createConfigurationContext(config)
}