package com.tfg.securerouter.data.app.screens.router_selector.model

import kotlinx.serialization.Serializable

@Serializable
data class RouterInfo(
    val name: String,
    val mac: String,
    val localIp: String? = null,
    val publicIpOrDomain: String? = null,
    val isVpn: Boolean,
    val id: Int,
    val installerPackage: String? = null,
)