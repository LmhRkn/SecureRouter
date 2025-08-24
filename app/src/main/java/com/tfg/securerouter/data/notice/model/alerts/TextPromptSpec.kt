package com.tfg.securerouter.data.notice.model.alerts

data class TextPromptSpec(
    val title: String,
    val message: String,
    val placeholder: String = "",
    val initialText: String = "",
    val confirmText: String = "Aceptar",
    val cancelText: String = "Cancelar",
    val showCancel: Boolean = true
)
