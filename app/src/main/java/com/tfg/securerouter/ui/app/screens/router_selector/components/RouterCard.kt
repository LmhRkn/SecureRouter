package com.tfg.securerouter.ui.app.screens.router_selector.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo

@Composable
fun RouterCard (
    router: RouterInfo,
    onRouterSelected: (RouterInfo) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onRouterSelected(router) },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nombre: ${router.name}", style = MaterialTheme.typography.bodyLarge)
            Text("IP: ${router.localIp}")
            Text("Tipo: ${if (router.isVpn) "VPN" else "Local"}")
        }
    }
}