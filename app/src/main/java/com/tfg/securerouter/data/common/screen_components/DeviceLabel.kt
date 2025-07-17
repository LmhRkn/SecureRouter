package com.tfg.securerouter.data.common.screen_components

import com.tfg.securerouter.R

enum class DeviceLabel(val displayName: String) {
    Phone(R.string.device_label_phone.toString()),
    PC(R.string.device_label_pc.toString()),
    Console(R.string.device_label_console.toString()),
    Online(R.string.device_label_online.toString()),
    Offline(R.string.device_label_offline.toString()),
    Blocked(R.string.device_label_blocked.toString());

    override fun toString(): String = displayName
}
