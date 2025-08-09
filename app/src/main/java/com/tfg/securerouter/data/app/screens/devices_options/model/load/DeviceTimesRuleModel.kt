package com.tfg.securerouter.data.app.screens.devices_options.model.load

import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRuleState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRulesState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.parseDaysToString
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
        val rules: MutableMap<Int, MutableMap<String, String>> = mutableMapOf()
        val lineRe = Regex(
            pattern = """firewall\.@rule\[(\d+)]\.(\w+)=['"]?([^'"\n]*)['"]?""",
            option = RegexOption.IGNORE_CASE
        )

        lineRe.findAll(output).forEach { m ->
            val idx = m.groupValues[1].toInt()
            val key = m.groupValues[2]
            val value = m.groupValues[3]
            val bag = rules.getOrPut(idx) { mutableMapOf() }
            bag[key] = value
        }

        val newRules = rules.entries
            .filter { entry ->
                val m = entry.value
                (m.containsKey("start_time") || m.containsKey("stop_time")) &&
                        m["name"]?.startsWith("time_mac_") == true
            }
            .map { (idx, m) ->
                DeviceTimesRuleState(
                    index = idx,
                    start = m["start_time"].orEmpty(),
                    finish = m["stop_time"].orEmpty(),
                    days = parseDaysToString(m["weekdays"].orEmpty()),
                    mac = m["name"]?.split("_")?.getOrNull(2).orEmpty()
                )
            }
            .sortedBy { it.index }

        return DeviceTimesRulesState(rules = newRules)
    }
}