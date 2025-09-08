package com.tfg.securerouter.ui.app.screens.router_selector.components.extras

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Router
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.R
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.data.app.screens.router_selector.RouterLabel
import com.tfg.securerouter.ui.app.common.card.StatusIndicators
import androidx.compose.material.icons.filled.VpnLock

@Composable
fun RouterCard(
    router: RouterInfo,
    onClick: () -> Unit,
) {
    val isOnline = RouterLabel.Online in router.labels
    var isNew by remember(router.mac, router.labels) {
        mutableStateOf(RouterLabel.New in router.labels)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val icon = when {
                    isOnline && router.isVpn -> Icons.Filled.VpnLock
                    else -> Icons.Filled.Router
                }
                val iconTint = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

                Icon(
                    imageVector = icon,
                    contentDescription = if (router.isVpn) "Router por VPN" else "Router",
                    modifier = Modifier
                        .size(50.dp)
                        .padding(end = 12.dp),
                    tint = iconTint
                )


                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nombre: ${router.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if(isOnline) Text(text = "IP: ${router.localIp}")
                }
            }
        }

        StatusIndicators(
            isNew = isNew,
            isOnline = isOnline,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp),
            newContentDescription = stringResource(R.string.device_label_new)
        )
    }
}
