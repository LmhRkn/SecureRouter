package com.tfg.securerouter.ui.app.screens.devices_options.components

import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache


@Composable
fun DeviceOptionsData(
    mac: String?,
    onEditAliasClick: (String) -> Unit
) {
//    val
//    EditableTextField(
//        text = state.deviceAlias ?: state.macAddress,
//        onTextSaved = { newAlias -> onEditAliasClick(newAlias) },
//        modifier = Modifier.fillMaxWidth()
//    )
//
//    Spacer(modifier = Modifier.height(4.dp))
//
//    Text(
//        state.macAddress,
//        style = MaterialTheme.typography.bodyMedium,
//        color = MaterialTheme.colorScheme.onBackground
//    )
    if (mac != null)
        parsDeviceOptionsState(mac)
}

fun parsDeviceOptionsState(mac: String) {
    var deviceData: String = ""
    DeviceManagerCache.get(mac)?.let {
        deviceData = it.toString()
    }
    println(deviceData)
}