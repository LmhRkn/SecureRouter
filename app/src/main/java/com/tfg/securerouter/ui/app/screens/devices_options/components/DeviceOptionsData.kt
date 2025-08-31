package com.tfg.securerouter.ui.app.screens.devices_options.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.json.jsons.device_manager.DeviceManagerCache
import com.tfg.securerouter.ui.app.common.texts.EditableTextField
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.devices_options.model.send.ChangeDeviceName
import com.tfg.securerouter.ui.app.screens.devices_options.components.extras.DeviceTypePicker
import androidx.compose.runtime.*
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.deviceTypes

@Composable
fun DeviceOptionsData(
    mac: String,
    onEditAliasClick: (String) -> Unit,
    topRightIcon: ImageVector = Icons.Filled.Edit,
) {
    val deviceData = DeviceManagerCache.getDevice(mac) ?: return

    var showTypePicker by rememberSaveable(mac) { mutableStateOf(false) }

    val currentTypeLabel: DeviceLabel? = remember(deviceData.labels) {
        when {
            deviceData.labels.contains(DeviceLabel.Phone) -> DeviceLabel.Phone
            deviceData.labels.contains(DeviceLabel.PC) -> DeviceLabel.PC
            deviceData.labels.contains(DeviceLabel.Console) -> DeviceLabel.Console
            deviceData.labels.contains(DeviceLabel.Other) -> DeviceLabel.Other
            else -> null
        }
    }

    Spacer(Modifier.height(20.dp))

    if (showTypePicker) {
        DeviceTypePicker(
            currentTypeLabel = currentTypeLabel,
            types = deviceTypes.toList(),
            onCancel = { showTypePicker = false },
            onSave = { newType ->
                DeviceManagerCache.update(mac) {d ->
                    d.copy(
                        labels = (d.labels - (currentTypeLabel ?: DeviceLabel.Other)) + newType.label,
                        icon = newType.icon,
                        iconDescription = newType.descriptionRes
                    )
                }
                showTypePicker = false
            }
        )
    } else {
        MainView(
            deviceData = deviceData,
            onEditAliasClick = onEditAliasClick,
            topRightIcon = topRightIcon,
            onIconClick = { showTypePicker = true }
        )
    }
}

@Composable
private fun MainView(
    deviceData: DeviceModel,
    onEditAliasClick: (String) -> Unit,
    topRightIcon: ImageVector,
    onIconClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Box(modifier = Modifier.size(56.dp)) {
            IconButton(
                onClick = onIconClick,
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                deviceData.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = deviceData.iconDescription?.let { id -> stringResource(id) }
                    )
                }
            }
            IconButton(
                onClick = onIconClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = (-6).dp)
                    .size(28.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Icon(imageVector = topRightIcon, contentDescription = null)
            }
        }

        Spacer(Modifier.width(16.dp))

        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
            EditableTextField(
                text = deviceData.hostname ?: "",
                onTextSaved = { newAlias ->
                    onEditAliasClick(newAlias)
                    ChangeDeviceName.updateDeviceAlias(
                            mac = deviceData.mac,
                            newAlias = newAlias
                        )
                },
            )

            Spacer(Modifier.height(6.dp))

            Row(modifier = Modifier.align(Alignment.Start)) {
                Text(text = deviceData.mac, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.width(8.dp))
                Text(text = deviceData.ip, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
