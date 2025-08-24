package com.tfg.securerouter.data.automatization.automatizations.on_vpn

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class VPNConfigureNetwork(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val saved = router?.installerPackage
        if (saved.isNullOrBlank()) return 1

        val checkCmd = """
            uci show network.vpn
        """.trimIndent()

        val out = sh(checkCmd).trim()
        return if (out.contains("uci: Entry not found")) 1 else -1

    }

    override suspend fun execute(): Boolean {
        val VPN_IF="vpn"
        val VPN_PORT="51820"
        val VPN_ADDR="192.168.9.1/24"
        val VPN_ADDR6="fd00:9::1/64"

        val BASE = "vpn"
        val DIR = "$BASE/router"
        val PRIV = "$DIR/wgserver.key"

        val script = """    
            uci -q delete network.${VPN_IF}
            uci set network.${VPN_IF}="interface"
            uci set network.${VPN_IF}.proto="wireguard"
            uci set network.${VPN_IF}.private_key="${'$'}(cat "$PRIV")"
            uci set network.${VPN_IF}.listen_port="${VPN_PORT}"
            uci add_list network.${VPN_IF}.addresses="${VPN_ADDR}"
            uci add_list network.${VPN_IF}.addresses="${VPN_ADDR6}"
        """.trimIndent()


        val result = sh(script)
        return true
    }
}
