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
        return safeLoad(
            cache = sharedCache,
            command = "uci show firewall",
            cacheKey = "firewall_rules_raw_wifi_fitler_web",
            parse = { output -> parseWifiFilterWebRules(output) },
            setState = { rulesState ->
                sharedCache["wifi_filter_web_rule_list"] = rulesState
                _state.value = rulesState
            }
        )
    }


    private fun parseWifiFilterWebRules(output: String): WifiFilterWebsRulesState {
        val rulesByIdx: MutableMap<Int, MutableMap<String, String>> = mutableMapOf()
        val lineRe = Regex(
            pattern = """firewall\.@rule\[(\d+)]\.(\w+)=['"]?([^'"\n]*)['"]?""",
            options = setOf(RegexOption.IGNORE_CASE)
        )
        for (m in lineRe.findAll(output)) {
            val idx = m.groupValues[1].toInt()
            val key = m.groupValues[2]
            val value = m.groupValues[3]
            val bag = rulesByIdx.getOrPut(idx) { mutableMapOf() }
            bag[key] = value
        }

        val WEB_PREFIX = "web_filter_"
        val rules = rulesByIdx.entries
            .mapNotNull { (idx, m) ->
                val name = m["name"] ?: return@mapNotNull null
                if (!name.startsWith(WEB_PREFIX)) return@mapNotNull null
                val domain = name.removePrefix(WEB_PREFIX)
                WifiFilterWebRuleState(index = idx, domain = domain)
            }
            .sortedBy { it.index }

        val nextIdx = nextFirewallIndexFromOutput(output)
        println("REGLAS: $rules")
        return WifiFilterWebsRulesState(rules = rules, nextIndex = nextIdx)
    }

    private fun nextFirewallIndexFromOutput(output: String): Int {
        val re = Regex("""(?m)^firewall\.@rule\[(\d+)]""")
        val maxIdx = re.findAll(output)
            .mapNotNull { it.groupValues[1].toIntOrNull() }
            .maxOrNull() ?: -1
        return maxIdx + 1
    }
}