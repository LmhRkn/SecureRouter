package com.tfg.securerouter.ui.router.router_list.model

data class RouterUIModel(
    val id: Int,
    val name: String,
    val isConnected: Boolean,
    val isVpn: Boolean,
    val error: Boolean
)
