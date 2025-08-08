package com.tfg.securerouter.ui.app.common.tables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.common.screen_components.rule_table.RuleTableModel

@Composable
fun RuleTable(
    rules: List<RuleTableModel>,
    onAddRule: () -> Unit,
    onRemoveRule: (RuleTableModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp, max = 400.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {
            FlowRow(
                maxItemsInEachRow = Int.MAX_VALUE,
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                rules.forEach { rule ->
                    RuleTableCard(
                        text = rule.title,
                        onButtonClicked = { onRemoveRule(rule) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddRule,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.wifi_add_rule_button))
        }
    }
}

fun removeItemRuleTable(
    rules: List<RuleTableModel>,
    ruleToRemove : RuleTableModel
): List<RuleTableModel> {
    return rules.filterNot { it == ruleToRemove }
}
