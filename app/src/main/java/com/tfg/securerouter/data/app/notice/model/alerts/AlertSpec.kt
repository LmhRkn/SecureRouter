package com.tfg.securerouter.data.app.notice.model.alerts

import androidx.compose.runtime.Immutable
import com.tfg.securerouter.R

@Immutable
data class AlertSpec(
    val title: Int,
    val message: Int? = null,
    val confirmText: Int = R.string.accept_button,
    val cancelText: Int = R.string.cancel_button,
    val showCancel: Boolean = true,

    val isMonospace: Boolean = false,
    val fontScale: Float = 0.8f,
    val softWrap: Boolean = false,
    val maxHeightDp: Int = 360,

    val imageBase64: String? = null
)