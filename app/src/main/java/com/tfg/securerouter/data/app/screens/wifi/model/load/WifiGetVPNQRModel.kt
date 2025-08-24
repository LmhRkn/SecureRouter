package com.tfg.securerouter.data.app.screens.wifi.model.load

import android.util.Log
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.utils.AppSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class VPNQrState(
    val peerFolder: String = "",
    val confText: String = "",
    val qrPngBase64: String? = null,
    val error: String? = null
)

class WifiGetVPNQRModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(
        VPNQrState(peerFolder = "peer_${AppSession.nextDeviceVPN}")
    )
    val state: StateFlow<VPNQrState> = _state

    override suspend fun loadData(): Boolean {
        val peerFolder = "peer_${AppSession.nextDeviceVPN}"
        val file = "/root/vpn/devices/$peerFolder/client.conf"

        // Solo leemos el conf con marcadores fiables; nada de base64/qrencode
        val cmd = """
        printf "__CONF_START__"
        qrencode -t ansiutf8 < $file
        printf "__CONF_END__"

    """.trimIndent()

        return safeLoad(
            cache = sharedCache,
            command = cmd,
            cacheKey = "vpn_conf_$peerFolder",
            parse = { output -> parseConfOutput(output, peerFolder) },
            setState = { st -> _state.value = st }
        )
    }

    private fun parseConfOutput(raw: String, peer: String): VPNQrState {
        val body = raw.substringAfter("__CONF_START__", "")
            .substringBefore("__CONF_END__", "")
        if (body.isBlank()) {
            return VPNQrState(peerFolder = peer, error = "client.conf vacío o inaccesible")
        }
        return VPNQrState(peerFolder = peer, confText = body)
    }
}
