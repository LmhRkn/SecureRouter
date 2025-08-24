package com.tfg.securerouter.data.app.screens.wifi.model.load

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebsRulesState
import com.tfg.securerouter.data.app.screens.wifi.model.vpn.WifiVPNPeersState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiGetVPNPeers(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow<List<WifiVPNPeersState>>(emptyList())
    val state: StateFlow<List<WifiVPNPeersState>> = _state


    override suspend fun loadData(): Boolean {
        val cmd = "uci -q show network || true"

        return safeLoad(
            cache = sharedCache,
            command = cmd,
            cacheKey = "wifi_vpn_peers",
            parse = { output -> parseVPNPeers(output) },
            setState = { peers ->
                sharedCache["wifi_vpn_peers"] = peers
                _state.value = peers
            }
        )
    }

    private fun parseVPNPeers(raw: String): List<WifiVPNPeersState> {
        data class Acc(
            val name: String,
            val ips: MutableList<String> = mutableListOf(),
            var publicKey: String? = null,
            var description: String? = null
        )

        val peers = mutableMapOf<String, Acc>()

        val sectionRe   = Regex("""^network\.([^.]+)=wireguard_vpn$""")
        val allowedRe   = Regex("""^network\.([^.]+)\.allowed_ips=(.+)$""")
        val publicKeyRe = Regex("""^network\.([^.]+)\.public_key='([^']*)'$""")
        val descRe      = Regex("""^network\.([^.]+)\.description='([^']*)'$""")
        val quotedRe    = Regex("""'([^']+)'""")

        raw.lineSequence().forEach { line ->
            when {
                sectionRe.matches(line) -> {
                    val name = sectionRe.matchEntire(line)!!.groupValues[1]
                    peers.getOrPut(name) { Acc(name) }
                }
                allowedRe.matches(line) -> {
                    val (name, rest) = allowedRe.matchEntire(line)!!.destructured
                    val ips = quotedRe.findAll(rest).map { it.groupValues[1] }.toList()
                    val acc = peers.getOrPut(name) { Acc(name) }
                    acc.ips.addAll(ips)
                }
                publicKeyRe.matches(line) -> {
                    val (name, pk) = publicKeyRe.matchEntire(line)!!.destructured
                    peers.getOrPut(name) { Acc(name) }.publicKey = pk
                }
                descRe.matches(line) -> {
                    val (name, d) = descRe.matchEntire(line)!!.destructured
                    peers.getOrPut(name) { Acc(name) }.description = d
                }
            }
        }

        return peers.values
            .map { acc ->
                val uniqueIps = acc.ips.distinct()
                val ipv4 = uniqueIps.firstOrNull { it.contains('.') } ?: uniqueIps.firstOrNull().orEmpty()
                WifiVPNPeersState(name = acc.name, ip = ipv4)
            }
            .sortedBy { it.name }
    }
}
