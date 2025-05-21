package com.tfg.securerouter.ui.app.main_menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.menu.MenuRegistry

@Composable
fun DrawerContent(
    visible: Boolean = false,
    onItemClick: (String) -> Unit,
    topPadding: Dp = 0.dp // nuevo
) {
    if (visible) Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .padding(top = topPadding)
            .fillMaxHeight()
            .fillMaxWidth(0.85f)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            itemsIndexed(MenuRegistry.items) { index, menuOption ->
                DrawerItem(label = stringResource(id=menuOption.titleResId), icon = menuOption.icon) {
                    onItemClick(menuOption.route)
                }
                if (index < MenuRegistry.items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }

        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    onItemClick: (String) -> Unit
) {
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

