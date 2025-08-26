package com.tfg.securerouter.data.app.automatization.automatizations.on_vpn

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault

class VPNConfigureFirewall(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val saved = router?.installerPackage
        if (saved.isNullOrBlank()) return 1

        val checkCmd = """
            uci show firewall.wg
        """.trimIndent()

        val out = sh(checkCmd).trim()
        Log.d("VPNConfigureFirewallLog", "out: $out")
        return if (out.contains("uci: Entry not found")) 1 else -1
    }
    override suspend fun execute(): Boolean {
        val VPN_IF="vpn"
        val VPN_PORT="51820"

        val script = """
            VPN_IF=$VPN_IF
            VPN_PORT=$VPN_PORT
            
            uci rename firewall.@zone[0]="lan"
            uci rename firewall.@zone[1]="wan"
            uci del_list firewall.lan.network="${VPN_IF}"
            uci add_list firewall.lan.network="${VPN_IF}"
            uci -q delete firewall.wg
            uci set firewall.wg="rule"
            uci set firewall.wg.name="Allow-WireGuard"
            uci set firewall.wg.src="wan"
            uci set firewall.wg.dest_port="${VPN_PORT}"
            uci set firewall.wg.proto="udp"
            uci set firewall.wg.target="ACCEPT"
            
            uci commit firewall
            service firewall restart
""".trimIndent()


        val result = sh(script)
        return true
    }
}
