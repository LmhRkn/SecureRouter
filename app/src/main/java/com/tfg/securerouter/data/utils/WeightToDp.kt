package com.tfg.securerouter.data.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Calculates the width in dp based on a given weight and maximum width constraint.
 *
 * Usage:
 * Converts a proportional [weight] into an absolute width using the device's screen width
 * and a specified [maxWidth] limit.
 *
 * @param maxWidth The maximum width constraint in dp.
 * @param weight The proportion (0.0f to 1.0f) of the width to allocate.
 * @return The computed width in dp.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun width_weight_to_dp(
    maxWidth: Dp,
    weight: Float
): Dp {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    return weight_to_dp(maxLength = maxWidth, weight = weight, screenHeightDp = screenWidthDp)
}

/**
 * Calculates the height in dp based on a given weight and maximum height constraint.
 *
 * Usage:
 * Converts a proportional [weight] into an absolute height using the device's screen height
 * and a specified [maxHeight] limit.
 *
 * @param maxHeight The maximum height constraint in dp.
 * @param weight The proportion (0.0f to 1.0f) of the height to allocate.
 * @return The computed height in dp.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun height_weight_to_dp(
    maxHeight: Dp,
    weight: Float
): Dp {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    return weight_to_dp(maxLength = maxHeight, weight = weight, screenHeightDp = screenHeightDp)
}

/**
 * Helper function that converts a weight into a dp value based on screen size and a maximum constraint.
 *
 * This function is used internally by [width_weight_to_dp] and [height_weight_to_dp].
 *
 * @param maxLength The maximum size constraint (width or height) in dp.
 * @param weight The proportion (0.0f to 1.0f) of the size to allocate.
 * @param screenHeightDp The screen dimension (width or height) in dp.
 * @return The computed size in dp.
 */
@Composable
private fun weight_to_dp(
    maxLength: Dp,
    weight: Float,
    screenHeightDp: Dp
): Dp {
    val screenDp = screenHeightDp
    val density = LocalDensity.current

    val parentDp = with(density) { maxLength }
    val minDp = minOf(screenDp, parentDp)

    val allocatedHeightDp = minDp * weight

    return allocatedHeightDp
}
