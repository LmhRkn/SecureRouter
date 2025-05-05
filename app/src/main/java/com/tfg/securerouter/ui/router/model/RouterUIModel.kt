package com.tfg.securerouter.ui.router.model

data class RouterUIModel(
    val id: Int,
    val name: String,
    val isConnected: Boolean,
    val isVpn: Boolean
)
