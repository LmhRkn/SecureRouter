package com.tfg.securerouter.ui.common.searchbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.common.screen_components.DeviceLabel

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
    var expanded by remember { mutableStateOf(false) }
    val allFilters = DeviceLabel.values().toList()
    val currentFilters = remember { mutableStateOf(selectedFilters.toMutableSet()) }

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
                        // Toggle filter and create a new Set for recomposition
                        val updated = currentFilters.value.toMutableSet()
                        if (updated.contains(label)) updated.remove(label) else updated.add(label)
                        currentFilters.value = updated

                        // Notify parent immediately
                        onFiltersChanged(updated.toSet())
                    }
                )
            }
        }
    }
}

