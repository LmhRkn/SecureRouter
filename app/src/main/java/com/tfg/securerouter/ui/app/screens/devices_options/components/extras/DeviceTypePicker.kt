package com.tfg.securerouter.ui.app.screens.devices_options.components.extras

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.common.screen_components.devices.*
import com.tfg.securerouter.data.utils.height_weight_to_dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DeviceTypePicker(
    currentTypeLabel: DeviceLabel?,
    types: List<DeviceTypeConfig> = deviceTypes.toList(),
    onCancel: () -> Unit,
    onSave: (DeviceTypeConfig) -> Unit
) {
    val initialIndex = remember(currentTypeLabel, types) {
        val idx = types.indexOfFirst { it.label == currentTypeLabel }
        if (idx >= 0) idx else types.indexOfFirst { false }.coerceAtLeast(0)
    }

    var selectedIndex by rememberSaveable(currentTypeLabel) { mutableStateOf(initialIndex) }
    val canSave = selectedIndex != initialIndex

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.device_type_picker_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 120.dp,
                    max = height_weight_to_dp(
                        maxHeight = LocalConfiguration.current.screenHeightDp.dp,
                        weight = 0.5f
                    )
                ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            itemsIndexed(types) { index, config ->
                val selected = index == selectedIndex

                Card(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable { selectedIndex = index },
                    border = if (selected)
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 6.dp else 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = config.icon,
                            contentDescription = stringResource(config.descriptionRes),
                            modifier = Modifier.size(28.dp),
                            tint = if (selected)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = labelText(config),
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.cancel_button)) }

            Button(
                onClick = { onSave(types[selectedIndex]) },
                enabled = canSave,
                modifier = Modifier.weight(1f)
            ) { Text(stringResource(R.string.save_button)) }
        }
    }
}

@Composable
private fun labelText(config: DeviceTypeConfig): String =
    when (config.label) {
        DeviceLabel.Phone   -> stringResource(R.string.device_label_phone)
        DeviceLabel.PC      -> stringResource(R.string.device_label_pc)
        DeviceLabel.Console -> stringResource(R.string.device_label_console)
        else                -> stringResource(R.string.device_label_other)
    }
