package com.tfg.securerouter.data.app.common.screen_components.devices

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class para definir los tipos de dispositivos y sus propiedades.
 */
data class DeviceTypeConfig(
    val keywords: List<String>,
    val icon: ImageVector,
    val descriptionRes: Int,
    val label: DeviceLabel?
)