package com.tfg.securerouter.data.app.screens.devices_options.alerts

import com.tfg.securerouter.data.app.notice.model.alerts.AlertSpec

fun BlockDeviceAlert(): AlertSpec = AlertSpec(
        title = "¿Bloquear dispositivo?",
        message = "El dispositivo perderá acceso a la red.",
        confirmText = "Bloquear",
        cancelText = "Cancelar",
        showCancel = true
    )
