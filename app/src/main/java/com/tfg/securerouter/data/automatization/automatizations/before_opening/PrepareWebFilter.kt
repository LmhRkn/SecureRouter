package com.tfg.securerouter.data.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault

class PrepareDnsFirewall(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override val timeoutMs: Long = 100_000L

    private fun lastNonEmpty(out: String): String =
        out.lineSequence().map { it.trim() }.lastOrNull { it.isNotEmpty() }.orEmpty()

    private suspend fun shLastLine(cmd: String): String =
        lastNonEmpty(sh("( { $cmd ; } 2>/dev/null || true) | tail -n 1"))

    private suspend fun uciGet(key: String): String =
        shLastLine("uci -q get $key || echo ''")

    private suspend fun sectionExists(section: String): Boolean =
        shLastLine("[ -n \"$(uci -q show $section 2>/dev/null)\" ] && echo 1 || echo 0") == "1"

    private suspend fun hasDnsIntercept(): Boolean {
        if (!sectionExists("firewall.dns_int")) return false
        val src      = uciGet("firewall.dns_int.src")
        val srcDport = uciGet("firewall.dns_int.src_dport")
        val destPort = uciGet("firewall.dns_int.dest_port")
        val target   = uciGet("firewall.dns_int.target")
        val ok = (src == "lan" && srcDport == "53" && destPort == "53" && target == "DNAT")
        Log.d("PrepareDnsFirewall", "hasDnsIntercept=$ok")
        return ok
    }

    private suspend fun hasDotBlock(): Boolean {
        if (!sectionExists("firewall.dot_fwd")) return false
        val src      = uciGet("firewall.dot_fwd.src")
        val dest     = uciGet("firewall.dot_fwd.dest")
        val destPort = uciGet("firewall.dot_fwd.dest_port")
        val target   = uciGet("firewall.dot_fwd.target")
        val ok = (src == "lan" && dest == "wan" && destPort == "853" && target == "REJECT")
        Log.d("PrepareDnsFirewall", "hasDotBlock=$ok")
        return ok
    }

    private suspend fun isApplied(): Int {
        val ok = hasDnsIntercept() && hasDotBlock()
        Log.d("PrepareDnsFirewall", "isApplied=$ok")
        return if (ok) -1 else 1
    }

    override suspend fun shouldRun(router: RouterInfo?): Int = isApplied()

    override suspend fun execute(): Boolean {
        sh("""
            uci -q delete firewall.dns_int
            uci set firewall.dns_int=redirect
            uci set firewall.dns_int.name='Intercept-DNS'
            uci set firewall.dns_int.family='any'
            uci set firewall.dns_int.proto='tcp udp'
            uci set firewall.dns_int.src='lan'
            uci set firewall.dns_int.src_dport='53'
            uci set firewall.dns_int.dest_port='53'
            uci set firewall.dns_int.target='DNAT'

            uci -q delete firewall.dot_fwd
            uci set firewall.dot_fwd=rule
            uci set firewall.dot_fwd.name='Deny-DoT'
            uci set firewall.dot_fwd.src='lan'
            uci set firewall.dot_fwd.dest='wan'
            uci set firewall.dot_fwd.dest_port='853'
            uci set firewall.dot_fwd.proto='tcp udp'
            uci set firewall.dot_fwd.target='REJECT'

            uci commit firewall
            service firewall restart
        """.trimIndent())

        val ok = hasDnsIntercept() && hasDotBlock()
         return ok
    }
}
