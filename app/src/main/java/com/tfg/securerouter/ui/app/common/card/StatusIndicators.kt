package com.tfg.securerouter.ui.app.common.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StatusIndicators(
    isNew: Boolean,
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    newContentDescription: String? = null,
    spacing: Dp = 6.dp,
    newIconSize: Dp = 16.dp,
    onlineDotSize: Dp = 12.dp,
    newTint: Color = MaterialTheme.colorScheme.primary,
    onlineColor: Color = Color(0xFF4CAF50),
    onlineBorderWidth: Dp = 2.dp,
    onlineBorderColor: Color = MaterialTheme.colorScheme.surface
) {
    if (!isNew && !isOnline) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isNew) {
            Icon(
                imageVector = Icons.Filled.NewReleases,
                contentDescription = newContentDescription,
                tint = newTint,
                modifier = Modifier.size(newIconSize)
            )
        }
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(onlineDotSize)
                    .background(onlineColor, CircleShape)
                    .border(onlineBorderWidth, onlineBorderColor, CircleShape)
            )
        }
    }
}
