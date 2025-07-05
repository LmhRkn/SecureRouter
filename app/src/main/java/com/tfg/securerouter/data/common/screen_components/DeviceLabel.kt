package com.tfg.securerouter.data.common.screen_components

import com.tfg.securerouter.R

enum class DeviceLabel(val displayName: String) {
    Phone(R.string.device_label_phone.toString()),
    Labtop(R.string.device_label_laptop.toString()),
    Online(R.string.device_label_online.toString()),
    Offline(R.string.device_label_offline.toString()),
    Blocked(R.string.device_label_blocked.toString());

    override fun toString(): String = displayName
}
