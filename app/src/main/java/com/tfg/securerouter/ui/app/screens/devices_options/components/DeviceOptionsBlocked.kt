package com.tfg.securerouter.ui.app.screens.devices_options.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel

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
