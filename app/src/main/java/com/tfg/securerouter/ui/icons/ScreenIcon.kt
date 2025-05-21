package com.tfg.securerouter.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val ScreenIcon: ImageVector
    get() = materialIcon(name = "Filled.ScreenSimple") {
        materialPath {
            // Rectángulo de la pantalla
            moveTo(4f, 6f)
            lineTo(20f, 6f)
            verticalLineTo(16f)
            lineTo(4f, 16f)
            close()

            // Base
            moveTo(10f, 17f)
            horizontalLineTo(14f)
            verticalLineTo(18f)
            horizontalLineTo(10f)
            close()

            // Soporte/base inferior
            moveTo(8f, 19f)
            horizontalLineTo(16f)
            verticalLineTo(20f)
            horizontalLineTo(8f)
            close()
        }
    }
