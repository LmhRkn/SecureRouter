package com.tfg.securerouter.data.app.screens.devices_options.model.load

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRuleState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRulesState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.nextDays
import com.tfg.securerouter.data.app.common.screen_components.devices.model.parseDaysToString
import com.tfg.securerouter.data.app.common.screen_components.devices.model.parseStringToDays
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceTimesRuleModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(DeviceTimesRulesState())
    val state: StateFlow<DeviceTimesRulesState> = _state

    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "uci show firewall",
            cacheKey = "firewall_rules_raw",
            parse = { output -> parseTimesDevicesRules(output) },
            setState = { rulesState ->
                sharedCache["device_time_rule_list"] = rulesState
                _state.value = rulesState
            }
        )
    }


    private fun parseTimesDevicesRules(output: String): DeviceTimesRulesState {
        val rulesByIdx: MutableMap<Int, MutableMap<String, String>> = mutableMapOf()
        val lineRe = Regex(
            pattern = """firewall\.@rule\[(\d+)]\.(\w+)=['"]?([^'"\n]*)['"]?""",
            option = RegexOption.IGNORE_CASE
        )
        lineRe.findAll(output).forEach { m ->
            val idx = m.groupValues[1].toInt()
            val key = m.groupValues[2]
            val value = m.groupValues[3]
            val bag = rulesByIdx.getOrPut(idx) { mutableMapOf() }
            bag[key] = value
        }

        val raw = rulesByIdx.entries
            .filter { (_, m) ->
                (m.containsKey("start_time") || m.containsKey("stop_time")) &&
                        m["name"]?.startsWith("time_mac_") == true
            }
            .map { (idx, m) ->
                DeviceTimesRuleState(
                    index = idx,
                    start = m["start_time"].orEmpty(),
                    finish = m["stop_time"].orEmpty(),
                    days = parseDaysToString(m["weekdays"].orEmpty()),
                    mac = m["src_mac"].orEmpty().ifBlank {
                        m["name"]?.split("_")?.getOrNull(2).orEmpty()
                    }
                )
            }
            .sortedBy { it.index }

        val usedAsTail = mutableSetOf<Int>()
        val merged = mutableListOf<DeviceTimesRuleState>()

        fun expectedTailDays(headDaysLetters: String): String {
            val routerDays = parseStringToDays(headDaysLetters)
            val shifted = nextDays(routerDays)
            return parseDaysToString(shifted)
        }

        for (r in raw) {
            val isHead = r.start != "00:00:00" && r.finish == "23:59:59"
            val isTail = r.start == "00:00:00" && r.finish != "23:59:59"

            if (isTail && r.index in usedAsTail) {
                continue
            }

            if (isHead) {
                val expectDaysTail = expectedTailDays(r.days)
                val tail = raw.firstOrNull { t ->
                    t.mac.equals(r.mac, ignoreCase = true) &&
                            t.start == "00:00:00" &&
                            t.finish != "23:59:59" &&
                            t.days == expectDaysTail &&
                            t.index > r.index &&
                            t.index !in usedAsTail
                }

                if (tail != null) {
                    merged += DeviceTimesRuleState(
                        index = r.index,
                        index2 = tail.index,
                        start = r.start,
                        finish = tail.finish,
                        days = r.days,
                        mac = r.mac
                    )
                    usedAsTail += tail.index
                    continue
                }
            }

            if (isTail && r.index in usedAsTail) continue
            merged += r.copy(index2 = null)
        }

        val nextIdx = nextFirewallIndexFromOutput(output)
        return DeviceTimesRulesState(
            rules = merged.sortedBy { it.index },
            nextIndex = nextIdx
        )
    }

    private fun nextFirewallIndexFromOutput(output: String): Int {
        val re = Regex("""(?m)^firewall\.@rule\[(\d+)]""")
        val maxIdx = re.findAll(output)
            .mapNotNull { it.groupValues[1].toIntOrNull() }
            .maxOrNull() ?: -1
        return maxIdx + 1
    }

}