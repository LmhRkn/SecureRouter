package com.tfg.securerouter.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun width_weight_to_dp(
    maxWidth: Dp,
    weight: Float
): Dp {
    // Obtener tamaño de pantalla en dp
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp


    return weight_to_dp(max_length = maxWidth, weight = weight, screenHeightDp = screenWidthDp)
}

@Composable
fun height_weight_to_dp(
    maxHeight: Dp,
    weight: Float
): Dp {
    // Obtener tamaño de pantalla en dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    return weight_to_dp(max_length = maxHeight, weight = weight, screenHeightDp = screenHeightDp)
}
@Composable
fun height_weight_to_dp2(
    maxHeight: Dp,
    weight: Float
): Dp {
    // Obtener tamaño de pantalla en dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    val density = LocalDensity.current

    // Tamaño disponible para este componente
    val parentHeightDp = with(density) { maxHeight }

    // Tomar el tamaño más pequeño entre pantalla y contenedor
    val minHeightDp = minOf(screenHeightDp, parentHeightDp)

    // Si hay un total de N weights, aquí asumimos solo el peso actual
    val allocatedHeightDp = minHeightDp * weight

    return allocatedHeightDp
}

@Composable
private fun weight_to_dp(
    max_length: Dp,
    weight: Float,
    screenHeightDp: Dp
): Dp {
    val screenDp = screenHeightDp

    val density = LocalDensity.current

    // Tamaño disponible para este componente
    val parentDp = with(density) { max_length }

    // Tomar el tamaño más pequeño entre pantalla y contenedor
    val minDp = minOf(screenDp, parentDp)

    // Si hay un total de N weights, aquí asumimos solo el peso actual
    val allocatedHeightDp = minDp * weight

    return allocatedHeightDp
}