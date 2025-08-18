package com.tfg.securerouter.data.app.screens.router_selector

import com.tfg.securerouter.R

enum class RouterLabel(val displayName: String) {
    Online(R.string.device_label_online.toString()),
    Offline(R.string.device_label_offline.toString()),
    New(R.string.device_label_new.toString());

    override fun toString(): String = displayName
}
