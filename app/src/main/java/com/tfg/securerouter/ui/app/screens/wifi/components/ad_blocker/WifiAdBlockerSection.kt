package com.tfg.securerouter.ui.app.screens.wifi.components.ad_blocker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.ui.app.common.buttons.ToggleButton

@Composable
fun WifiAdBlockerSection(
    router: RouterInfo?
) {
    if (router == null) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Text(
            text = "Bloqueador de anuncions:",
            style = MaterialTheme.typography.bodyLarge,
        )
        ToggleButton(
            checked = false, // TODO: como saberlo
            onCheckedChange = { /* TODO */ }
        )
    }
}