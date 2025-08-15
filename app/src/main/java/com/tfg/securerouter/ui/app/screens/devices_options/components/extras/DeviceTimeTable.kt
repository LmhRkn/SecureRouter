package com.tfg.securerouter.ui.app.screens.devices_options.components.extras

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.tfg.securerouter.ui.app.common.tables.RuleTable
import androidx.compose.runtime.saveable.Saver
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRuleState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRulesState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toReadableList
import com.tfg.securerouter.data.app.screens.devices_options.model.send.RemoveTimeRuleDevice

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceTimeTable(
    deviceTimesRule: DeviceTimesRulesState,
    mac: String
) {
    val ruleSaver = Saver<List<DeviceTimesRuleState>, List<String>>(
        save = { list ->
            list.map { r -> "${r.start}§${r.finish}§${r.days}§${r.mac}§${r.index}§${r.index2 ?: ""}" }
        },
        restore = { encoded ->
            encoded.map { s ->
                val p = s.split('§')
                DeviceTimesRuleState(
                    start  = p.getOrNull(0).orEmpty(),
                    finish = p.getOrNull(1).orEmpty(),
                    days   = p.getOrNull(2).orEmpty(),
                    mac    = p.getOrNull(3).orEmpty(),
                    index  = p.getOrNull(4)?.toIntOrNull() ?: -1,
                    index2 = p.getOrNull(5)?.toIntOrNull()
                )
            }
        }
    )

    var ruleToEdit by remember { mutableStateOf(DeviceTimesRuleState()) }

    var rules by rememberSaveable(mac, stateSaver = ruleSaver) {
        mutableStateOf(emptyList<DeviceTimesRuleState>())
    }

    var isAddingRule by rememberSaveable { mutableStateOf(false) }
    var isEditingRule by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(mac, deviceTimesRule.rules) {
        val noSep = mac.lowercase().replace(Regex("[^0-9a-f]"), "")
        val withColons = noSep.chunked(2).joinToString(":")

        val incoming = deviceTimesRule.rules
            .filter { rule -> mac.isBlank() || rule.mac.lowercase() == withColons }

        if (rules.isEmpty()) {
            rules = incoming
        } else {
            val idsActuales = rules.map { it.index to it.index2 }.toSet()
            val nuevos = incoming.filter { (it.index to it.index2) !in idsActuales }
            rules = rules + nuevos
        }
    }

    val nextIndex = calcNextIndexPreferLocal(deviceTimesRule.nextIndex, rules)
    if (isAddingRule) {
        AddDeviceTime(
            onSave = { rule ->
                rules = rules + rule
                isAddingRule = false
            },
            onBumpToEnd = { title ->
                val pos = rules.indexOfFirst { it.toReadableList().trim() == title.trim() }
                if (pos != -1) {
                    val tmp = rules.toMutableList()
                    val moved = tmp.removeAt(pos)
                    tmp.add(moved)
                    rules = tmp
                }
                isAddingRule = false
            },
            onCancel = {
                isAddingRule = false
            },
            mac = mac,
            nextIndex = nextIndex,
            currentRules = rules
        )
    }
    else if (isEditingRule) {
        AddDeviceTime(
            oldRule = ruleToEdit,
            onSave = { rule ->
                rules = rules + rule
                isEditingRule = false
            },
            onBumpToEnd = { title ->
                val pos = rules.indexOfFirst { it.toReadableList().trim() == title.trim() }
                if (pos != -1) {
                    val tmp = rules.toMutableList()
                    val moved = tmp.removeAt(pos)
                    tmp.add(moved)
                    rules = tmp
                }
                isEditingRule = false
            },
            onCancel = {
                isEditingRule = false
            },
            onRemoveRule = { ruleToRemove ->
                RemoveTimeRuleDevice.removeTimeRuleDevice(ruleToRemove.index, ruleToRemove.index2)

                rules = removeRuleAndShift(rules, ruleToRemove)
            },
            mac = mac,
            nextIndex = nextIndex,
            currentRules = rules
        )
    } else {
        RuleTable(
            rules = rules,
            onAddRule = {
                isAddingRule = true
                isEditingRule = false
            },
            onRemoveRule = { ruleToRemove ->
                RemoveTimeRuleDevice.removeTimeRuleDevice(ruleToRemove.index, ruleToRemove.index2)
                rules = removeRuleAndShift(rules, ruleToRemove)
            },
            onCardClick = { ruleClicked ->
                isAddingRule = false
                isEditingRule = true
                ruleToEdit = ruleClicked
            }
        )
    }
}

fun nextFirewallIndexFromDump(dump: String): Int {
    val re = Regex("""firewall\.@rule\[(\d+)]""")
    val maxRouter = re.findAll(dump)
        .mapNotNull { it.groupValues[1].toIntOrNull() }
        .maxOrNull() ?: -1
    return maxRouter + 1
}

fun nextFirewallIndex(dump: String, localRules: List<DeviceTimesRuleState>): Int {
    val re = Regex("""firewall\.@rule\[(\d+)]""")
    val maxRouter = re.findAll(dump)
        .mapNotNull { it.groupValues[1].toIntOrNull() }
        .maxOrNull() ?: -1
    val maxLocal = localRules.maxOfOrNull { it.index } ?: -1
    return maxOf(maxRouter, maxLocal) + 1
}

private fun calcNextIndexPreferLocal(baseFromRouter: Int, localRules: List<DeviceTimesRuleState>): Int {
    val localMax = localRules.maxOfOrNull { maxOf(it.index, it.index2 ?: -1) } ?: -1
    return if (localMax >= 0) localMax + 1 else (baseFromRouter - 1) + 1
}

private fun removeRuleAndShift(
    rules: List<DeviceTimesRuleState>,
    toRemove: DeviceTimesRuleState
): List<DeviceTimesRuleState> {
    val removedPrimary = toRemove.index
    val removedSecondary = toRemove.index2
    val removedSlots = if (removedSecondary != null) 2 else 1
    val upperBound = removedSecondary ?: removedPrimary

    return rules
        .filterNot { it.index == removedPrimary && it.index2 == removedSecondary }
        .map { r ->
            val newIndex  = if (r.index  > upperBound) r.index  - removedSlots else r.index
            val newIndex2 = r.index2?.let { if (it > upperBound) it - removedSlots else it }
            r.copy(index = newIndex, index2 = newIndex2)
        }
        .sortedWith(compareBy<DeviceTimesRuleState> { it.index }.thenBy { it.index2 ?: Int.MAX_VALUE })
}
