package com.tfg.securerouter.data.app.screens.wifi.model.load

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebsRulesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiFilterWebRuleModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(WifiFilterWebsRulesState())
    val state: StateFlow<WifiFilterWebsRulesState> = _state

    override suspend fun loadData(): Boolean {
        val cmd = """
            (uci -q show dhcp.@dnsmasq[0].address || true)
            echo '-----FW-----'
            (uci -q show firewall || true)
        """.trimIndent()

        return safeLoad(
            cache = sharedCache,
            command = cmd,
            cacheKey = "wifi_filter_web_rules_raw_v2",
            parse = { output -> parseGlobalAndMacRules(output) },
            setState = { rulesState ->
                sharedCache["wifi_filter_web_rule_list"] = rulesState
                _state.value = rulesState
            }
        )
    }

    private fun parseGlobalAndMacRules(output: String): WifiFilterWebsRulesState {
        val parts = output.split("\n-----FW-----\n")
        val dnsmasqRaw = parts.getOrNull(0).orEmpty()
        val firewallRaw = parts.getOrNull(1).orEmpty()

        val globals = parseDnsmasqGlobals(dnsmasqRaw)
        val macRules = parseFirewallMacRules(firewallRaw)

        val all = (globals + macRules).sortedWith(
            compareBy<WifiFilterWebRuleState> { it.mac != null }
                .thenBy { it.domain }
                .thenBy { it.mac ?: "" }
        )
        return WifiFilterWebsRulesState(
            rules = all,
            nextIndex = all.size
        )
    }

    private fun parseDnsmasqGlobals(raw: String): List<WifiFilterWebRuleState> {
        val lineRe = Regex(
            pattern = """^dhcp\.[^=]+\.address=['"]/([^/]+)/(0\.0\.0\.0|127\.0\.0\.1|::)['"]$""",
            options = setOf(RegexOption.MULTILINE)
        )
        val out = mutableListOf<WifiFilterWebRuleState>()
        var idx = 0
        for (m in lineRe.findAll(raw)) {
            val domain = m.groupValues[1].trim()
            out += WifiFilterWebRuleState(
                index = idx++,
                domain = domain,
                mac = null,
                source = "dnsmasq"
            )
        }
        return out
    }

    private fun parseFirewallMacRules(raw: String): List<WifiFilterWebRuleState> {
        val rulesByIdx: MutableMap<Int, MutableMap<String, String>> = mutableMapOf()
        val lineRe = Regex(
            pattern = """^firewall\.@rule\[(\d+)]\.(\w+)=['"]?([^'"\n]*)['"]?""",
            options = setOf(RegexOption.MULTILINE)
        )
        for (m in lineRe.findAll(raw)) {
            val idx = m.groupValues[1].toInt()
            val key = m.groupValues[2]
            val value = m.groupValues[3]
            val bag = rulesByIdx.getOrPut(idx) { mutableMapOf() }
            bag[key] = value
        }

        val WEB_PREFIX = "web_filter_"
        val out = mutableListOf<WifiFilterWebRuleState>()
        var logicalIdx = 0

        val nameMacRe = Regex("""^${WEB_PREFIX}(.+?)_mac_([0-9A-Fa-f:]{17}|[0-9A-Fa-f]{12})$""")

        for ((_, m) in rulesByIdx) {
            val name = m["name"] ?: continue
            val srcMac = m["src_mac"] ?: continue

            if (!name.startsWith(WEB_PREFIX)) continue

            val (domain, macFromName) = nameMacRe.matchEntire(name)?.let {
                it.groupValues[1] to it.groupValues[2]
            } ?: (name.removePrefix(WEB_PREFIX) to srcMac)

            out += WifiFilterWebRuleState(
                index = logicalIdx++,
                domain = domain,
                mac = macFromName.ifEmpty { srcMac },
                source = "firewall"
            )
        }
        return out
    }
}
