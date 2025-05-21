package com.tfg.securerouter.ui.icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val FilterIcon: ImageVector
    get() = materialIcon(name = "Custom.Filter") {
        materialPath {
            // Parte superior ancha (entrada del filtro)
            moveTo(4f, 6f)
            lineTo(20f, 6f)
            lineTo(14f, 12f)

            // Cuerpo del filtro (embudo)
            lineTo(14f, 18f)
            lineTo(10f, 18f)
            lineTo(10f, 12f)
            close()
        }
    }
