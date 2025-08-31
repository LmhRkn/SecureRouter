package com.tfg.securerouter.data.app.screens.router_selector.model

import com.tfg.securerouter.data.app.screens.router_selector.RouterLabel
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
    val labels: Set<RouterLabel> = emptySet(),
    val sshPassword: String? = null,

    val vpnHost: String? = null,
    val vpnPort: Int? = 22,
    val sshHostKeyFingerprint: String? = null,

    val shownTutorials: Set<String> = emptySet()

)