package com.tfg.securerouter.data.notice.model.alerts

import androidx.compose.runtime.Immutable

@Immutable
data class AlertSpec(
    val title: String,
    val message: String? = null,
    val confirmText: String = "Aceptar",
    val cancelText: String = "Cancelar",
    val showCancel: Boolean = true,

    val isMonospace: Boolean = false,
    val fontScale: Float = 0.8f,
    val softWrap: Boolean = false,
    val maxHeightDp: Int = 360,

    val imageBase64: String? = null
)