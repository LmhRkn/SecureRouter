package com.tfg.securerouter.ui.app.common.searchbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel

/**
 * Composable function for a filter button with a dropdown menu of selectable options.
 *
 * Usage:
 * This component displays an [IconButton] with a filter icon. When clicked, it opens a dropdown menu
 * containing checkboxes for all available [DeviceLabel] options. The user can toggle filters, and
 * changes are propagated via [onFiltersChanged].
 *
 * @param modifier Modifier applied to the button container. Defaults to [Modifier].
 * @param selectedFilters The current set of selected [DeviceLabel] filters.
 * @param onFiltersChanged Lambda invoked with the updated set of filters whenever a change occurs.
 * @param iconButton The icon displayed in the button. Defaults to [Icons.Default.FilterList].
 * @param contentDescription Content description for accessibility of the filter icon.
 * @param iconColor Color applied to the filter icon. Defaults to [MaterialTheme.colorScheme.primary].
 * @param checkedColor Color of checkboxes when selected. Defaults to [MaterialTheme.colorScheme.primary].
 * @param uncheckedColor Color of checkboxes when unselected. Defaults to [MaterialTheme.colorScheme.primary].
 *
 * @see DeviceLabel
 */

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    selectedFilters: Set<DeviceLabel>,
    onFiltersChanged: (Set<DeviceLabel>) -> Unit,
    iconButton: ImageVector = Icons.Default.FilterList,
    contentDescription: String = "Abrir filtros",
    iconColor: Color = MaterialTheme.colorScheme.primary,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = MaterialTheme.colorScheme.primary
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val allFilters = DeviceLabel.values().toList()
    val currentFilters = rememberSaveable { mutableStateOf(selectedFilters.toMutableSet()) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = iconButton,
                contentDescription = contentDescription,
                tint = iconColor
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(220.dp)
        ) {
            allFilters.forEach { label ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = currentFilters.value.contains(label),
                                onCheckedChange = null, // handled in onClick
                                colors = CheckboxDefaults.colors(
                                    checkedColor = checkedColor,
                                    uncheckedColor = uncheckedColor
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(id = label.displayName.toInt()))
                        }
                    },
                    onClick = {
                        val updated = currentFilters.value.toMutableSet()
                        if (updated.contains(label)) updated.remove(label) else updated.add(label)
                        currentFilters.value = updated

                        onFiltersChanged(updated.toSet())
                    }
                )
            }
        }
    }
}

