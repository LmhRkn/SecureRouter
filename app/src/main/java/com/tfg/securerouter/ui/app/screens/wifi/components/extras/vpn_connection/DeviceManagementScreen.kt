package com.tfg.securerouter.ui.app.screens.wifi.components.extras.vpn_connection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.data.app.screens.wifi.model.send.vpn.DelVPNPeer
import com.tfg.securerouter.ui.app.screens.wifi.components.vpn_conection.DeletePeerPanel
import com.tfg.securerouter.R

@Composable
fun DeviceManagementScreen(
    onAddDevice: () -> Unit,
    wifiCoordinator: WifiCoordinator,
) {
    var deletePeer by remember { mutableStateOf(false) }

    if (deletePeer) {
        DeletePeerPanel(
            wifiCoordinator = wifiCoordinator,
            onCancel = { deletePeer = false },
            onConfirmDelete = { name, ip ->
                DelVPNPeer.delVPNPeer(ip = ip, name = name)
                deletePeer = false
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onAddDevice) { Text(stringResource(R.string.wifi_vpn_add_device)) }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { deletePeer = true }) { Text(stringResource(R.string.wifi_vpn_del_device)) }
        }
    }
}
