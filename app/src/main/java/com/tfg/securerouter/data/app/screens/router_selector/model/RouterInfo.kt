package com.tfg.securerouter.data.app.screens.router_selector.model

data class RouterInfo(
    val name: String,
    val mac: String,
    val localIp: String?,
    val publicIpOrDomain: String?,
    val isVpn: Boolean,
    val id: Int,
)
