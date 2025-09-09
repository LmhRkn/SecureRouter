package com.tfg.securerouter.ui.app.screens.wifi.components.vpn_conection

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
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiGetVPNPeers
import com.tfg.securerouter.R

@Composable
fun DeletePeerPanel(
    wifiCoordinator: WifiCoordinator,
    onCancel: () -> Unit,
    onConfirmDelete: (name: String, ip: String) -> Unit,
    listHeightDp: Int = 320
) {
    val peersModule = remember(wifiCoordinator) {
        wifiCoordinator.modules.filterIsInstance<WifiGetVPNPeers>().first()
    }
    val peers by peersModule.state.collectAsState(initial = emptyList())

    var query by remember { mutableStateOf("") }
    var selected: com.tfg.securerouter.data.app.screens.wifi.model.vpn.WifiVPNPeersState? by remember { mutableStateOf(null) }

    val filtered = remember(query, peers) {
        peers.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.ip.contains(query, ignoreCase = true)
        }.sortedBy { it.name.lowercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.wifi_vpn_delete_peer), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        androidx.compose.material3.OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.wifi_vpn_search_name_ip)) },
            label = { Text(stringResource(R.string.wifi_vpn_search)) }
        )

        Spacer(Modifier.height(8.dp))

        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(listHeightDp.dp)
        ) {
            items(
                items = filtered,
                key = { it.name }
            ) { peer ->
                PeerRow(
                    name = peer.name,
                    ip = peer.ip,
                    selected = selected?.name == peer.name,
                    onClick = { selected = peer }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            androidx.compose.material3.OutlinedButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel_button))
            }
            Spacer(Modifier.width(8.dp))
            Button(
                enabled = selected != null,
                onClick = {
                    selected?.let { onConfirmDelete(it.name, it.ip) }
                }
            ) {
                Text(stringResource(R.string.delete_button))
            }
        }
    }
}

@Composable
private fun PeerRow(
    name: String,
    ip: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .then(Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
            Text(text = ip, style = MaterialTheme.typography.bodyMedium)
        }
    }
}