package com.tfg.securerouter.data.app.notice.model.alerts

data class ActiveAlert(
    val spec: AlertSpec,
    val onConfirm: () -> Unit = {},
    val onCancel: () -> Unit = {}
)