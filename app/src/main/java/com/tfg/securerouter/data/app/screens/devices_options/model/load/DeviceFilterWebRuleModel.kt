package com.tfg.securerouter.data.app.screens.devices_options.model.load

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebRuleState
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebsRulesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceFilterWebRuleModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(DeviceFilterWebsRulesState())
    val state: StateFlow<DeviceFilterWebsRulesState> = _state

    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "uci show firewall",
            cacheKey = "firewall_rules_raw_device_fitler_web",
            parse = { output -> parseDeviceFilterWebRules(output) },
            setState = { rulesState ->
                sharedCache["device_filter_web_rule_list"] = rulesState
                _state.value = rulesState
            }
        )
    }


    private fun parseDeviceFilterWebRules(output: String): DeviceFilterWebsRulesState {
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

        val NAME_PREFIXES = listOf("web_filter_", "time_filter_")

        val rules = rulesByIdx.entries
            .mapNotNull { (idx, m) ->
                val name = m["name"] ?: return@mapNotNull null
                val prefix = NAME_PREFIXES.firstOrNull { name.startsWith(it) } ?: return@mapNotNull null

                var tail = name.removePrefix(prefix).trim()
                tail = tail.trimEnd('}', '"', '\'')
                val macFromNameCandidate = tail.substringBefore('_', "")
                val domainFromName = if ('_' in tail) tail.substringAfter('_') else tail

                var mac = normalizeMac(m["src_mac"].orEmpty())
                if (mac.isEmpty()) {
                    val n = normalizeMac(macFromNameCandidate)
                    if (n.isNotEmpty()) mac = n
                }

                val domain = domainFromName

                DeviceFilterWebRuleState(
                    index  = idx,
                    mac    = mac,
                    domain = domain
                )
            }
            .sortedBy { it.index }

        val nextIdx = nextFirewallIndexFromOutput(output)
        return DeviceFilterWebsRulesState(rules = rules, nextIndex = nextIdx)
    }

    private fun normalizeMac(raw: String): String {
        val hex = raw.lowercase().filter { it in '0'..'9' || it in 'a'..'f' }
        return if (hex.length == 12) hex.chunked(2).joinToString(":") else ""
    }


    private fun nextFirewallIndexFromOutput(output: String): Int {
        val re = Regex("""(?m)^firewall\.@rule\[(\d+)]""")
        val maxIdx = re.findAll(output)
            .mapNotNull { it.groupValues[1].toIntOrNull() }
            .maxOrNull() ?: -1
        return maxIdx + 1
    }
}