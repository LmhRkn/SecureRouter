package com.tfg.securerouter.data.app.screens.devices_options.alerts

import com.tfg.securerouter.data.notice.model.alerts.AlertSpec

fun UnblockDeviceAlert(): AlertSpec = AlertSpec(
        title = "¿Desbloquear dispositivo?",
        message = "El dispositivo volverá a tener acceso a la red.",
        confirmText = "Desbloquear",
        cancelText = "Cancelar",
        showCancel = true
)
