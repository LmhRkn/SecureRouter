package com.tfg.securerouter.ui.app.screens.devices_options.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.devices_options.model.send.BlockDevice
import com.tfg.securerouter.data.app.screens.devices_options.model.send.UnblockDevice
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.utils.TimeUtils.blockedNowHuman

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceOptionsBlockButton(
    deviceModel: DeviceModel,
    modifier: Modifier = Modifier,
    onChanged: (DeviceModel) -> Unit
) {
    val isBlocked = DeviceLabel.Blocked in deviceModel.labels
    val text = if (isBlocked) "Desbloquear dispositivo" else "Bloquear dispositivo"
    val icon = if (isBlocked) Icons.Outlined.LockOpen else Icons.Outlined.Lock

    Button(
        onClick = {
            if (isBlocked) {
                UnblockDevice.unblockDevice(deviceModel.mac)
                val updated = deviceModel.copy(
                    labels = deviceModel.labels - DeviceLabel.Blocked,
                    blockedAt = null
                )
                DeviceManagerCache.update(updated.mac, {
                    updated
                })
                onChanged(updated)
            } else {
                BlockDevice.blockDevice(deviceModel.mac)
                val updated = deviceModel.copy(
                    labels = deviceModel.labels + DeviceLabel.Blocked,
                    blockedAt = "Bloqueado ${blockedNowHuman()}"
                )
                DeviceManagerCache.update(updated.mac, { updated })
                onChanged(updated)
            }
        },
        modifier = modifier,
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
