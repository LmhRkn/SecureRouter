package com.tfg.securerouter.ui.language.model

data class LanguageUIModel(
    val code: String,
    val displayName: String,
    val iconRes: Int? = null // opcional: R.drawable.ic_flag_fr
)
