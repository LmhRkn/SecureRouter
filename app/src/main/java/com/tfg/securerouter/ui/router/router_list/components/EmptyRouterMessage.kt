package com.tfg.securerouter.ui.router.router_list.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R

@Composable
fun EmptyRouterMessage() {
    Text(
        text = stringResource(id = R.string.no_routers_encontrados),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}