package com.tfg.securerouter.ui.components.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.state.HomeUiState

@Composable
fun HomeRouterInfoSection(state: HomeUiState, onEditAliasClick: () -> Unit) {
    println("Composing HomeContent with state 2: $state")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = state.routerAlias ?: state.routerIp,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(onClick = onEditAliasClick) {
            Icon(
                Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.home_router_Info_section_description),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        state.routerIp,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
//    Text("${state.routerIp} : ${state.macAddress}")
}