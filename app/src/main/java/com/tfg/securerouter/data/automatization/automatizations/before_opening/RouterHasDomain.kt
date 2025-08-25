package com.tfg.securerouter.data.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class RouterHasDomain(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 60_000L

    private var cachedPeerIndex: Int? = null

    private suspend fun findFirstPeerIndex(): Int? {
        val cmd = """
            for i in $(seq 2 254); do
              if [ -d "/root/vpn/devices/peer_${'$'}i" ]; then
                echo ${'$'}i
                break
              fi
            done
        """.trimIndent()

        val raw = sh(cmd)
        val idx = raw.lineSequence()
            .map { it.trim() }
            .firstOrNull { it.matches(Regex("""^\d{1,3}$""")) }
            ?.toIntOrNull()

        cachedPeerIndex = idx
        if (idx == null) Log.d("RouterHasDomain", "No peer directory found in /root/vpn/devices/peer_{2..254}")
        return idx
    }

    override suspend fun shouldRun(router: RouterInfo?): Int {
        if (router?.publicIpOrDomain != null) return -1
        val idx = cachedPeerIndex ?: findFirstPeerIndex()
        return if (idx != null) 1 else -1
    }

    override suspend fun execute(): Boolean {
        val routerId = AppSession.routerId?.toString() ?: return false
        val idx = cachedPeerIndex ?: findFirstPeerIndex() ?: return false

        val path = "/root/vpn/devices/peer_${idx}/client.conf"
        val clientConf = sh("""cat "$path" 2>/dev/null || true""")
        if (clientConf.isBlank()) {
            return false
        }

        val endpointLine = clientConf
            .lineSequence()
            .map { it.trim() }
            .firstOrNull { it.startsWith("Endpoint", ignoreCase = true) }
            ?: "return false"

        val endpointVal = endpointLine.substringAfter("=").trim()

        val host = if (endpointVal.startsWith("[")) {
            endpointVal.substringAfter("[").substringBefore("]").trim()
        } else {
            endpointVal.substringBefore(":").trim()
        }

        val normalizedDomain = host.lowercase()
        if (normalizedDomain.isBlank()) return false

        RouterSelectorCache.update(routerId) { r -> r.copy(publicIpOrDomain = normalizedDomain) }

        return true
    }
}
