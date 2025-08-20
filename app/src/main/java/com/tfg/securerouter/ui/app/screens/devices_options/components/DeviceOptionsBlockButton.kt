package com.tfg.securerouter.ui.app.screens.devices_options.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.devices_options.alerts.BlockDeviceAlert
import com.tfg.securerouter.data.app.screens.devices_options.alerts.UnblockDeviceAlert
import com.tfg.securerouter.data.app.screens.devices_options.model.send.BlockDevice
import com.tfg.securerouter.data.app.screens.devices_options.model.send.UnblockDevice
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.utils.TimeUtils.blockedNowHuman
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.ui.notice.alerts.AlertModal

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

    var activeAlert by remember { mutableStateOf<AlertSpec?>(null) }
    var pendingAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    fun blockAction() {
        BlockDevice.blockDevice(deviceModel.mac)
        val updated = deviceModel.copy(
            labels = deviceModel.labels + DeviceLabel.Blocked,
            blockedAt = "Bloqueado ${blockedNowHuman()}"
        )
        DeviceManagerCache.update(updated.mac) { updated }
        onChanged(updated)
    }

    fun unblockAction() {
        UnblockDevice.unblockDevice(deviceModel.mac)
        val updated = deviceModel.copy(
            labels = deviceModel.labels - DeviceLabel.Blocked,
            blockedAt = null
        )
        DeviceManagerCache.update(updated.mac) { updated }
        onChanged(updated)
    }

    Button(
        onClick = {
            if (isBlocked) {
                activeAlert = UnblockDeviceAlert()
                pendingAction = { unblockAction() }
            } else {
                activeAlert = BlockDeviceAlert()
                pendingAction = { blockAction() }
            }
        },
        modifier = modifier,
    ) {
        Icon(imageVector = icon, contentDescription = text)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }

    if (activeAlert != null) {
        AlertModal(
            spec = activeAlert!!,
            onConfirm = {
                pendingAction?.invoke()
                activeAlert = null
                pendingAction = null
            },
            onCancel = {
                activeAlert = null
                pendingAction = null
            }
        )
    }
}
