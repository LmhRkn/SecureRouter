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
fun DeviceOptionsBlocked(
    deviceModel: DeviceModel,
    modifier: Modifier = Modifier
) {
    val isBlocked by remember(deviceModel.mac, deviceModel.labels) {
        mutableStateOf(DeviceLabel.Blocked in deviceModel.labels)
    }

    if (!isBlocked || deviceModel.blockedAt == null) return

    Text(deviceModel.blockedAt)
}
