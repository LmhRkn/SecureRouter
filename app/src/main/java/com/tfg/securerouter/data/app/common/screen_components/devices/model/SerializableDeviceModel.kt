package com.tfg.securerouter.data.app.common.screen_components.devices.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializableDeviceModel(
    val mac: String,
    val hostname: String? = null,
    val ip: String,
    val iconId: String,
    val iconDescription: Int? = null,
    val labelIds: List<String> = emptyList()
)
