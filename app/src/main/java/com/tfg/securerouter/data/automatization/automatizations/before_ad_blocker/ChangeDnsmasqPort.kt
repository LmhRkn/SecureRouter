package com.tfg.securerouter.data.automatization.automatizations.before_ad_blocker

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class ChangeDnsmasqPort(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override val timeoutMs: Long = 100_000L

    private fun lastNonEmpty(out: String): String =
        out.lineSequence()
            .map { it.trim() }
            .lastOrNull { it.isNotEmpty() }
            .orEmpty()

    private suspend fun shLastLine(cmd: String): String =
        lastNonEmpty(sh("( { $cmd ; } 2>/dev/null || true) | tail -n 1"))

    private suspend fun isDnsmasqCorrectPort(): Int {
        val res = shLastLine("uci -q get dhcp.@dnsmasq[0].port 2>/dev/null | grep -qx 54 && echo 1 || echo 0")
        Log.d("ChangeDnsmasqPort", "isDnsmasqCorrectPort(last)='$res'")
        return if (res == "1") -1 else 1
    }

    override suspend fun shouldRun(router: RouterInfo?): Int {
        return isDnsmasqCorrectPort()
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val cmd = """
            uci set dhcp.@dnsmasq[0].port="54"
            uci commit dhcp
            service dnsmasq restart
            service odhcpd restart
        """.trimIndent()

        sh(cmd)

        return isDnsmasqCorrectPort() == -1
    }
}
