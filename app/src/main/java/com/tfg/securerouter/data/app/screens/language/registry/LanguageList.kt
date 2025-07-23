package com.tfg.securerouter.data.app.screens.language.registry

import com.tfg.securerouter.R

object AvailableLanguages {
    val availableLanguages = listOf(
        Language(R.string.language_spanish, R.string.language_spanish_abbreviation),
        Language(R.string.language_english, R.string.language_english_abbreviation)
    )
}

data class Language(
    val displayName: Int,
    val abbreviation: Int
)