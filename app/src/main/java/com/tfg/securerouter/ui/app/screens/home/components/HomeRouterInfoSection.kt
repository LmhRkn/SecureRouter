package com.tfg.securerouter.ui.app.screens.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.home.state.HomeRouterInfoState
import com.tfg.securerouter.ui.app.common.texts.EditableTextField

@Composable
fun HomeRouterInfoSection(
    state: HomeRouterInfoState,
    onEditAliasClick: (String) -> Unit
) {
    EditableTextField(
        text = state.routerAlias ?: state.macAddress,
        onTextSaved = { newAlias -> onEditAliasClick(newAlias) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        state.macAddress,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}