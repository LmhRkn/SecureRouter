package com.tfg.securerouter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.icons.StatusCircle
import com.tfg.securerouter.ui.icons.VpnBadge
import com.tfg.securerouter.ui.theme.SecureRouterTheme
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String,
    router_connected: Boolean,
    vpn_connected: Boolean,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            menuButton(onMenuClick)
        },
        actions = {
            statusConnecitons(router_connected, vpn_connected)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun menuButton(onMenuClick: () -> Unit) {
    IconButton(onClick = onMenuClick) {
        Icon(
            Icons.Default.Menu,
            contentDescription = stringResource(R.string.menu_ic_description)
        )
    }
}

@Composable
fun statusConnecitons(router_connected: Boolean, vpn_connected: Boolean) {
    Column (
        horizontalAlignment = Alignment.End
    ) {
        StatusCircle(connected = router_connected, size = 16.dp, borderWidth = 1.5.dp)

        Spacer(modifier = Modifier.height(4.dp))

        VpnBadge(
            connected = vpn_connected,
            paddingHorizontal = 8.dp,
            paddingVertical = 1.dp,
            borderWidth = 1.5.dp
        )
    }
}

@Composable
@Preview
fun TopBarPreview() {
    SecureRouterTheme {
        TopBar(
            scope = CoroutineScope(kotlinx.coroutines.Dispatchers.Main),
            drawerState = DrawerState(DrawerValue.Closed),
            title = "Home",
            router_connected = true,
            vpn_connected = false,
            onMenuClick = {}
        )
    }
}