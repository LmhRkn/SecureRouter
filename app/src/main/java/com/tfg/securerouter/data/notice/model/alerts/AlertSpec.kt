package com.tfg.securerouter.data.notice.model.alerts

import androidx.compose.runtime.Immutable

@Immutable
data class AlertSpec(
    val title: String,
    val message: String? = null,
    val confirmText: String = "Aceptar",
    val cancelText: String = "Cancelar",
    val showCancel: Boolean = true
)
