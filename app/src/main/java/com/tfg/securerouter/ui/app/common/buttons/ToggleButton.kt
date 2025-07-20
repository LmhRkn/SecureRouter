package com.tfg.securerouter.ui.app.common.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Composable function for a customizable toggle switch button.
 *
 * Usage:
 * This button provides a smooth animated toggle between "on" and "off" states.
 * It supports customization of colors, size, and external state handling through [onCheckedChange].
 * The visual style includes a rounded track and a circular thumb that slides when toggled.
 *
 * @param checked The initial checked state of the toggle. Defaults to `false`.
 * @param onCheckedChange Lambda to be invoked with the new state when the toggle is clicked.
 * @param activeColor The background color of the switch when it is "on". Defaults to [primary].
 * @param inactiveColor The background color of the switch when it is "off". Defaults to [onBackground].
 * @param thumbColor The color of the thumb (circular slider). Defaults to [inversePrimary].
 * @param modifier A [Modifier] for custom styling or layout adjustments. Defaults to an empty modifier.
 * @param buttonSize The height of the toggle switch. The width is automatically scaled. Defaults to `50.dp`.
 */
@Composable
fun ToggleButton(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.onBackground,
    thumbColor: Color = MaterialTheme.colorScheme.inversePrimary,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 50.dp,
) {
    var isChecked by remember { mutableStateOf(checked) }

    val switchHeight: Dp = buttonSize
    val switchWidth: Dp = buttonSize * 1.6f
    val thumbSize: Dp = buttonSize * 0.87f

    // Animates the thumb position smoothly when toggled
    val thumbOffset by animateDpAsState(
        targetValue = if (isChecked) (switchWidth - thumbSize) else 0.dp,
        label = "ThumbOffset"
    )

    Box(
        modifier = modifier
            .width(switchWidth)
            .height(switchHeight)
            .clip(RoundedCornerShape(50))
            .background(if (isChecked) activeColor else inactiveColor)
            .clickable {
                isChecked = !isChecked
                onCheckedChange(isChecked)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .padding(2.dp)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}
