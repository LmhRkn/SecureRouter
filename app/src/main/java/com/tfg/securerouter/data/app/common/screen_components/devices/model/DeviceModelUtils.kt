package com.tfg.securerouter.data.app.common.screen_components.devices.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun DeviceModel.toSerializable(): SerializableDeviceModel {
    return SerializableDeviceModel(
        mac = mac,
        hostname = hostname,
        ip = ip,
        iconId = iconToId(icon),
        iconDescription = iconDescription,
        labelIds = labels.map { it.name },
        blockedAt = blockedAt
    )
}

fun SerializableDeviceModel.toDeviceModel(): DeviceModel {
    return DeviceModel(
        mac = mac,
        hostname = hostname,
        ip = ip,
        icon = idToIcon(iconId),
        iconDescription = iconDescription,
        labels = labelIds.mapNotNull { id -> DeviceLabel.entries.find { it.name == id } }.toSet(),
        blockedAt = blockedAt
    )
}

fun iconToId(icon: ImageVector?): String {
    return when (icon) {
        Icons.Filled.PhoneAndroid -> "PhoneAndroid"
        Icons.Filled.Laptop -> "Laptop"
        Icons.Filled.SportsEsports -> "SportsEsports"
        Icons.Filled.DevicesOther -> "DevicesOther"
        else -> "DevicesOther"
    }
}

fun idToIcon(id: String): ImageVector {
    return when (id) {
        "PhoneAndroid" -> Icons.Filled.PhoneAndroid
        "Laptop" -> Icons.Filled.Laptop
        "SportsEsports" -> Icons.Filled.SportsEsports
        "DevicesOther" -> Icons.Filled.DevicesOther
        else -> Icons.Filled.DevicesOther
    }
}