package com.tfg.securerouter.ui.app.screens.wifi.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.home.state.HomeRouterInfoState
import com.tfg.securerouter.ui.app.common.texts.EditableTextField
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import com.tfg.securerouter.data.app.screens.wifi.model.WifiRouterInfoState
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiRouterInfoModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiRouterInfoSection(
    state: WifiRouterInfoState,
    onEditAliasClick: (String) -> Unit
) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Router,
            contentDescription = "Router",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            EditableTextField(
                text = state.routerAlias ?: state.macAddress,
                onTextSaved = { newAlias ->
                    if (newAlias != state.routerAlias) onEditAliasClick(newAlias) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                state.macAddress,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}