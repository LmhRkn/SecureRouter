package com.tfg.securerouter.ui.app.screens

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
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.menu.MenuRegistry

/**
 * Composable that renders the content of the navigation drawer.
 *
 * Displays a scrollable list of menu options defined in [MenuRegistry]. Each item
 * includes an icon and a localized label, and triggers navigation when clicked.
 *
 * @param visible Whether the drawer content should be displayed.
 * @param onItemClick Callback invoked with the route of the selected menu option.
 *
 * @see MenuRegistry
 * @see DrawerItem
 */
@Composable
fun DrawerContent(
    visible: Boolean = false,
    onItemClick: (String) -> Unit
) {
    if (visible) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            BoxWithConstraints {
                val maxHeight = this.maxHeight

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(max = maxHeight) // máximo alto dinámico
                ) {
                    itemsIndexed(MenuRegistry.items) { index, menuOption ->
                        DrawerItem(
                            label = stringResource(id = menuOption.titleResId),
                            icon = menuOption.icon,
                            onClick = { onItemClick(menuOption.route) }
                        )

                        if (index < MenuRegistry.items.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composable representing a single clickable item in the navigation drawer.
 *
 * Displays an icon and label side by side, styled to match the app’s primary theme.
 *
 * @param label The localized label text for the menu option.
 * @param icon The [ImageVector] representing the menu option’s icon.
 * @param onClick Callback invoked when the item is tapped.
 */
@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        tonalElevation = 0.dp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
