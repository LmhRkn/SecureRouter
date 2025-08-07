package com.tfg.securerouter.ui.app.screens.devices_options.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.devices_options.model.send.ChangeDeviceName.updateDeviceAlias
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.ui.app.common.texts.EditableTextField


@Composable
fun DeviceOptionsData(
    mac: String,
    onEditAliasClick: (String) -> Unit
) {
    val deviceData = DeviceManagerCache.getDevice(mac)

    Spacer(modifier = Modifier.height(20.dp))

    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = deviceData?.icon!!,
            contentDescription = stringResource(id=deviceData.iconDescription!!),
            modifier = Modifier.size(56.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            EditableTextField(
                text = deviceData.hostname ?: "",
                onTextSaved = { newAlias -> updateDeviceAlias(mac = deviceData.mac, newAlias = newAlias) },
            )

            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = deviceData.mac,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = deviceData.ip,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}