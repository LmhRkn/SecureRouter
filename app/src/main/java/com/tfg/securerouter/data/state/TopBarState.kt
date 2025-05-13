package com.tfg.securerouter.data.state

data class TopBarModel(
    val title: String = "Home",
    val routerConnected: Boolean = true,
    val vpnConnected: Boolean = false
)