package com.tfg.securerouter.ui.app.screens.wifi.components.extras.filter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebsRulesState
import com.tfg.securerouter.data.app.screens.wifi.model.filter.toReadableList
import com.tfg.securerouter.data.app.screens.wifi.model.send.AddFilterWebRuleWifi
import com.tfg.securerouter.data.app.screens.wifi.model.send.RemoveFilterWebRuleWifi
import com.tfg.securerouter.ui.app.common.tables.GenericRuleTable
import com.tfg.securerouter.ui.app.common.tables.calcNextIndexPreferLocalGeneric
import com.tfg.securerouter.ui.app.common.tables.removeRuleAndShiftGeneric

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiFilterWebTable(
    wifiFilterWebsRule: WifiFilterWebsRulesState,
) {
    GenericRuleTable<WifiFilterWebRuleState, WifiFilterWebsRulesState>(
        container = wifiFilterWebsRule,
        rulesOf = { it.rules },
        encode = { r ->
            listOf(
                r.domain,
                r.index.toString()
            ).joinToString("§")
        },
        decode = { s -> decodeWifiFilterWebRule(s) },
        identityOf = { it.index },
        labelOf = { it.toReadableList() },
        nextIndexFrom = { cont, local ->
            calcNextIndexPreferLocalGeneric(
                baseFromRouter = cont.nextIndex,
                localRules = local,
                indexOf = { it.index },
            )
        },
        removeAndShift = { current, toRemove ->
            removeRuleAndShiftGeneric(
                rules = current,
                toRemove = toRemove,
                indexOf = { it.index },
                recalc = { r, newIdx, newIdx2 -> r.copy(index = newIdx) },
            )
        },
        onRemoveRemote = { r -> RemoveFilterWebRuleWifi.removeFilterWebRuleWifi(r.index) },
        AddOrEdit = { oldRule, mac, nextIdx, current, onSave, onCancel, onBumpToEnd, onRemoveRule ->
            AddWifiFilterWeb(
                oldRule = oldRule,
                onSave = onSave,
                onBumpToEnd = onBumpToEnd,
                onCancel = onCancel,
                onRemoveRule = onRemoveRule,
                nextIndex = nextIdx,
                currentRules = current,
                saveTextButton = if (oldRule != null) stringResource(R.string.edit_button) else stringResource(
                    R.string.add_button
                ),
                explanationText = stringResource(R.string.table_block_device_text),
                onAddRemote = { r ->
                    AddFilterWebRuleWifi.addFilterWebRuleWifi(r)
                }
            )
        }
    )
}

private fun decodeWifiFilterWebRule(s: String): WifiFilterWebRuleState {
    val p = s.split('§')
    return WifiFilterWebRuleState(
        domain = p.getOrNull(0)?.trim().orEmpty(),
        index  = p.getOrNull(1)?.toIntOrNull() ?: -1,
    )
}