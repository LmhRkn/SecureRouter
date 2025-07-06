package com.tfg.securerouter.ui.common.searchbar

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.common.screen_components.DeviceLabel

@Composable
fun FilterButton(
    modifier: Modifier = Modifier,
    selectedFilters: Set<DeviceLabel>,
    onFiltersChanged: (Set<DeviceLabel>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val allFilters = DeviceLabel.values().toList()
    val currentFilters = remember { mutableStateOf(selectedFilters.toMutableSet()) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Abrir filtros",
                tint = MaterialTheme.colorScheme.primary
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
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.primary
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

