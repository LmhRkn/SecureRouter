package com.tfg.securerouter.ui.router.windows.home.components.home_top_bar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.router.windows.home.state.HomeUiState
import com.tfg.securerouter.ui.theme.LocalExtraColors

@Composable
fun VpnBadgeIcon(
    state: HomeUiState,
    text: String = stringResource(id = R.string.home_vpm_icon_string),
    fontSize: TextUnit = 10.sp
) {
    val backgroundColor = if (state.isVpnActive) LocalExtraColors.current.statusConnectedColor else LocalExtraColors.current.statusErrorColor
    val borderColor = if (state.isVpnActive) LocalExtraColors.current.onStatusConnectedColor else LocalExtraColors.current.onStatusErrorColor
    val textColor = if (state.isVpnActive) LocalExtraColors.current.onStatusConnectedColor else LocalExtraColors.current.onStatusErrorColor

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(50))
            .border(BorderStroke(1.5.dp, borderColor), shape = RoundedCornerShape(50))
            .padding(horizontal = 6.dp, vertical = 1.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize
        )
    }
}