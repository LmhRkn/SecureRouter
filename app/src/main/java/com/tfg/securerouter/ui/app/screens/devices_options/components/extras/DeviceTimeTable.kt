package com.tfg.securerouter.ui.app.screens.devices_options.components.extras

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.tfg.securerouter.data.app.common.screen_components.rule_table.RuleTableModel
import com.tfg.securerouter.ui.app.common.tables.RuleTable
import androidx.compose.runtime.saveable.Saver
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRulesState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toReadableList
import com.tfg.securerouter.data.app.screens.devices_options.model.send.RemoveTimeRuleDevice

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceTimeTable(
    deviceTimesRule: DeviceTimesRulesState,
    mac: String
) {
    val ruleSaver = Saver<List<RuleTableModel>, List<String>>(
        save = { list ->
            list.map { r -> "${r.title}§${r.index}§${r.index2 ?: ""}" }
        },
        restore = { encoded ->
            encoded.map { s ->
                val parts = s.split('§')
                RuleTableModel(
                    title = parts.getOrNull(0).orEmpty(),
                    index = parts.getOrNull(1)?.toIntOrNull() ?: -1,
                    index2 = parts.getOrNull(2)?.toIntOrNull()
                )
            }
        }
    )


    var rules by rememberSaveable(stateSaver = ruleSaver) {
        mutableStateOf(listOf())
    }
    var isAddingRule by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(mac, deviceTimesRule) {
        val noSep = mac.lowercase().replace(Regex("[^0-9a-f]"), "")
        val withColons = noSep.chunked(2).joinToString(":")
        rules = deviceTimesRule.rules
            .filter { rule -> mac.isBlank() || rule.mac.lowercase() == withColons }
            .map { rule ->
                RuleTableModel(
                    title = rule.toReadableList(),
                    index = rule.index,
                    index2 = rule.index2
                )
            }
    }
    val nextIndex = (rules.maxOfOrNull { it.index } ?: -1) + 1
    println(rules)
    if (isAddingRule) {
        AddDeviceTime(
            onSave = { rule ->
                rules = rules + rule
                isAddingRule = false
            },
            onBumpToEnd = { title ->
                val pos = rules.indexOfFirst { it.title.trim() == title.trim() }
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
    } else {
        RuleTable(
            rules = rules,
            onAddRule = {
                isAddingRule = true
            },
            onRemoveRule = { ruleToRemove ->
                rules = rules.filter { it != ruleToRemove }
                RemoveTimeRuleDevice.removeTimeRuleDevice(ruleToRemove.index, ruleToRemove.index2)
            },
            onCardClick = { ruleClicked ->
                println("Clic en tarjeta: ${ruleClicked.title}")
            }
        )
    }
}