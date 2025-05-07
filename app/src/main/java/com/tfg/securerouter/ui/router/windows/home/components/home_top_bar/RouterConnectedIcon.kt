package com.tfg.securerouter.ui.router.windows.home.components.home_top_bar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.router.windows.home.state.HomeUiState

@Composable
fun RouterConnectedIcon(state: HomeUiState) {
    Icon(
        painter = painterResource(id = if (state.isRouterConnected)
            R.drawable.ic_router_state_connected
        else R.drawable.ic_router_state_disconnected),
        contentDescription = stringResource(id = R.string.home_router_connected_icon_description),
        tint = Color.Unspecified
    )
}