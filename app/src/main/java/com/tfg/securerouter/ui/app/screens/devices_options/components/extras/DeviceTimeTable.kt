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
import com.tfg.securerouter.data.app.common.screen_components.rule_table.RuleTableModel
import com.tfg.securerouter.ui.app.common.tables.RuleTable
import com.tfg.securerouter.ui.app.common.tables.removeItemRuleTable
import androidx.compose.runtime.saveable.Saver

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceTimeTable() {
    val ruleSaver = Saver<List<RuleTableModel>, List<String>>(
        save = { it.map { rule -> rule.title } },
        restore = { it.map { title -> RuleTableModel(title) } }
    )

    var rules by rememberSaveable(stateSaver = ruleSaver) {
        mutableStateOf(listOf())
    }

    var isAddingRule by rememberSaveable { mutableStateOf(false) }

    // Simulación de reglas iniciales
    LaunchedEffect(Unit) {
        if (rules.isEmpty()) {
            rules = listOf(
                RuleTableModel("Bloquear los M,X,J de 20:00 a 08:00h"),
                RuleTableModel("Bloquear los V,S,D de 00:00 a 08:00h"),
                RuleTableModel("Bloquear los L,M,X,J,V,S,D de 14:00 a 17:00h")
            )
        }
    }

    if (isAddingRule) {
        AddDeviceTime(
            onSave = { rule ->
                rules = rules + rule
                isAddingRule = false
            },
            onCancel = {
                isAddingRule = false
            }
        )
    } else {
        RuleTable(
            rules = rules,
            onAddRule = {
                isAddingRule = true
            },
            onRemoveRule = { ruleToRemove ->
                rules = rules.filter { it != ruleToRemove }
            }
        )
    }
}
