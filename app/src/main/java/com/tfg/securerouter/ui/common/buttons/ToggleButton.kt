package com.tfg.securerouter.ui.common.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * A function for creating an toggle buttons
 *
 * Usage:
 * It adds a reusable toggle button.
 *
 * @property checked a [Boolean] representing the initial state of the toggle button.
 * @property onCheckedChange a lambda function that is triggered when the state of the toggle button changes.
 * @property activeColor a [Color] representing the color of the toggle button when it is checked.
 * @property inactiveColor a [Color] representing the color of the toggle button when it is not checked.
 * @property thumbColor a [Color] representing the color of the thumb of the toggle button.
 * @property modifier a [Modifier] for customizing the layout of the composable.
 * @property buttonSize a [Dp] representing the size of the toggle button.
 *
 */

@Composable
fun ToggleButton(
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = Color(0xFF2196F3),
    inactiveColor: Color = Color.LightGray,
    thumbColor: Color = Color.White,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 50.dp,
) {
    var isChecked by remember { mutableStateOf(checked) }


    val switchHeight: Dp = buttonSize
    val switchWidth: Dp = buttonSize * 1.6f
    val thumbSize: Dp = buttonSize * 0.87f

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

