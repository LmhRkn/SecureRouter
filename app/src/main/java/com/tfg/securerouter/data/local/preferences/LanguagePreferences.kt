package com.tfg.securerouter.data.local.preferences

import android.content.Context
import javax.inject.Inject

class LanguagePreferences @Inject constructor(context: Context) {
    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getSelectedLanguage(): String? {
        return prefs.getString("language", null)
    }

    fun saveLanguage(code: String) {
        prefs.edit().putString("language", code).apply()
    }
}
