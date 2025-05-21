package com.tfg.securerouter.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val WifiIcon: ImageVector
    get() = materialIcon(name = "Custom.WifiSharp") {
        materialPath {
            // Onda grande diagonal
            moveTo(4f, 10f)
            lineTo(5f, 8f)
            lineTo(8f, 5f)
            lineTo(12f, 4f)
            lineTo(16f, 5f)
            lineTo(19f, 8f)
            lineTo(20f, 10f)
            lineTo(18f, 12f)
            lineTo(17f, 10f)
            lineTo(15f, 8f)
            lineTo(12f, 7f)
            lineTo(9f, 8f)
            lineTo(7f, 10f)
            lineTo(6f, 12f)
            close()

            // Onda media diagonal
            moveTo(8f, 14f)
            lineTo(9f, 12f)
            lineTo(12f, 11f)
            lineTo(15f, 12f)
            lineTo(16f, 14f)
            lineTo(14.5f, 15.5f)
            lineTo(13.5f, 14.5f)
            lineTo(12f, 14f)
            lineTo(10.5f, 14.5f)
            lineTo(9.5f, 15.5f)
            close()

            // Onda pequeña
            moveTo(11f, 17f)
            lineTo(12f, 16.3f)
            lineTo(13f, 17f)
            lineTo(12f, 17.7f)
            close()

            // Punto central
            moveTo(12f, 20f)
            arcToRelative(1f, 1f, 0f, true, true, -0.01f, 0f)
        }
    }
