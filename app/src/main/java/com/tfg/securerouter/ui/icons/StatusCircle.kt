package com.tfg.securerouter.ui.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.theme.LocalExtraColors

@Composable
fun StatusCircle(
    connected: Boolean = true,
    size: Dp = 24.dp,
    borderWidth: Dp = 2.dp,
    modifier: Modifier = Modifier
) {
    val extraColors = LocalExtraColors.current
    val fillColor = if (connected) extraColors.connectedStatus else extraColors.disconnectedStatus
    val borderColor = if (connected) extraColors.onConnectedStatus else extraColors.onDisconnectedStatus

    Canvas(modifier = modifier.size(size)) {
        val radius = size.toPx() / 2
        drawCircle(
            color = fillColor,
            radius = radius
        )
        drawCircle(
            color = borderColor,
            style = Stroke(width = borderWidth.toPx())
        )
    }
}
