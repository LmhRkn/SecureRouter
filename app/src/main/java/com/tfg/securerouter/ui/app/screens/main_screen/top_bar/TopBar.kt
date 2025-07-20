package com.tfg.securerouter.ui.app.screens.main_screen.top_bar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.main_screen.state.TopBarModel
import com.tfg.securerouter.ui.icons.StatusCircle
import com.tfg.securerouter.ui.icons.VpnBadge
import com.tfg.securerouter.ui.theme.SecureRouterTheme

/**
 * Composable function representing the top bar of the application.
 *
 * Input:
 * - modifier: Optional [Modifier] to apply to the TopBar layout.
 * - topBarModel: [TopBarModel] containing UI state for the title, and connection status indicators.
 * - onMenuClick: Lambda function triggered when the navigation menu button is clicked.
 *
 * Output:
 * - Displays a center-aligned top app bar with:
 *   - A title in the center.
 *   - A menu icon on the left.
 *   - VPN and router connection indicators on the right.
 *
 * Notes:
 * - Uses Material 3 components and theming.
 * - Uses `menuButton()` and `statusConnections()` for modular UI.
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    topBarModel: TopBarModel,
    onMenuClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(topBarModel.title) },
        navigationIcon = {
            menuButton(onMenuClick)
        },
        actions = {
            statusConnecitons(topBarModel.routerConnected, topBarModel.vpnConnected)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

/**
 * Composable function representing the navigation menu button.
 *
 * Input:
 * - onMenuClick: Lambda function triggered when the navigation menu button is clicked.
 *
 * Output:
 * - An [IconButton] containing a menu icon with accessibility description.
 */
@Composable
fun menuButton(onMenuClick: () -> Unit) {
    IconButton(onClick = onMenuClick) {
        Icon(
            Icons.Default.Menu,
            contentDescription = stringResource(R.string.menu_ic_description)
        )
    }
}

/**
 * Composable function representing the connection status indicators.
 *
 * Input:
 * - router_connected: Boolean indicating if the router is connected.
 * - vpn_connected: Boolean indicating if the VPN is connected.
 *
 * Output:
 * - A column with:
 *   - A circular icon indicating router status (connected/disconnected).
 *   - A VPN badge showing VPN connection status.
 *
 * Notes:
 * - Aligned to the end (right) side of the top bar.
 */
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

/**
 * Preview function for the TopBar composable.
 * Displays a sample TopBar using mock data inside the SecureRouterTheme.
 */
@Composable
@Preview
fun TopBarPreview() {
    SecureRouterTheme {
        TopBar(
            topBarModel = TopBarModel(
                title = "Home",
                routerConnected = true,
                vpnConnected = false
            ),
            onMenuClick = {}
        )
    }
}