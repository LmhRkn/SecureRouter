package com.tfg.securerouter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class DrawerItem(val label: String, val icon: ImageVector)

@Composable
fun DrawerContent(
    visible: Boolean = false,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Administrar Dispositivos" to Icons.Default.Warning,
        "Config. WiFi" to Icons.Default.Warning,
        "Filtros" to Icons.Default.Warning,
        "Configuración" to Icons.Default.Settings
    )

    if (visible) Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.85f)
            .padding(top = 115.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            itemsIndexed(items) { index, (label, icon) ->
                DrawerItem(label = label, icon = icon, onItemClick = onItemClick)
                // Agregar Divider entre los ítems, excepto después del último
                if (index < items.lastIndex) {
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerItem(label: String, icon: ImageVector, onItemClick: (String) -> Unit) {
    Surface(
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(label) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 32.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

