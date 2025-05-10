package com.tfg.securerouter.ui.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.theme.LocalExtraColors

@Composable
fun VpnBadge(
    connected: Boolean = true,
    modifier: Modifier = Modifier,
    paddingHorizontal: Dp = 12.dp,
    paddingVertical: Dp = 6.dp,
    borderWidth: Dp = 2.dp,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall,
    text: String = stringResource(R.string.vpn_icon_string),
) {
    val extraColors = LocalExtraColors.current
    val backgroundColor = if (connected) extraColors.connectedStatus else extraColors.disconnectedStatus
    val borderColor = if (connected) extraColors.onConnectedStatus else extraColors.onDisconnectedStatus
    val textColor = if (connected) extraColors.onConnectedStatus else extraColors.onDisconnectedStatus

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(50))
            .border(borderWidth, borderColor, shape = RoundedCornerShape(50))
            .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
            fontWeight = FontWeight.Bold
        )
    }
}
