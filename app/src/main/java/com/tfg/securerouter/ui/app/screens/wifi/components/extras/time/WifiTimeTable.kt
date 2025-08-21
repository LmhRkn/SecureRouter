package com.tfg.securerouter.ui.app.screens.wifi.components.extras.time

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.wifi.model.send.time.RemoveTimeRuleWifi
import com.tfg.securerouter.data.app.screens.wifi.model.time.WifiTimesRuleState
import com.tfg.securerouter.data.app.screens.wifi.model.time.WifiTimesRulesState
import com.tfg.securerouter.data.app.screens.wifi.model.time.toReadableList
import com.tfg.securerouter.ui.app.common.tables.GenericRuleTable
import com.tfg.securerouter.ui.app.common.tables.calcNextIndexPreferLocalGeneric
import com.tfg.securerouter.ui.app.common.tables.removeRuleAndShiftGeneric

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiTimeTable(
    wifiTimesRule: WifiTimesRulesState,
) {
    GenericRuleTable<WifiTimesRuleState, WifiTimesRulesState>(
        container = wifiTimesRule,
        rulesOf = { it.rules },
        encode = { r ->
            listOf(
                r.start, r.finish, r.days,
                r.index.toString(), r.index2?.toString().orEmpty()
            ).joinToString("§")
        },
        decode = { s -> decodeWifiRule(s) },
        identityOf = { it.index to it.index2 },
        labelOf = { it.toReadableList() },
        nextIndexFrom = { cont, local ->
            calcNextIndexPreferLocalGeneric(
                baseFromRouter = cont.nextIndex,
                localRules = local,
                indexOf = { it.index },
                index2Of = { it.index2 }
            )
        },
        removeAndShift = { current, toRemove ->
            removeRuleAndShiftGeneric(
                rules = current,
                toRemove = toRemove,
                indexOf = { it.index },
                index2Of = { it.index2 },
                recalc = { r, newIdx, newIdx2 -> r.copy(index = newIdx, index2 = newIdx2) }
            )
        },
        onRemoveRemote = { r -> RemoveTimeRuleWifi.removeTimeRuleWifi(r.index, r.index2) },
        AddOrEdit = { oldRule, mac, nextIdx, current, onSave, onCancel, onBumpToEnd, onRemoveRule ->
            AddWifiTime(
                oldRule = oldRule,
                onSave = onSave,
                onBumpToEnd = onBumpToEnd,
                onCancel = onCancel,
                onRemoveRule = onRemoveRule,
                nextIndex = nextIdx,
                currentRules = current,
                saveTextButton = if (oldRule != null) stringResource(R.string.edit_button) else stringResource(R.string.add_button),
                explanationText = stringResource(R.string.table_block_device_text)
            )
        }
    )
}

private fun decodeWifiRule(s: String): WifiTimesRuleState {
    val p = s.split('§')
    return WifiTimesRuleState(
        start  = p.getOrNull(0).orEmpty(),
        finish = p.getOrNull(1).orEmpty(),
        days   = p.getOrNull(2).orEmpty(),
        index  = p.getOrNull(3)?.toIntOrNull() ?: -1,
        index2 = p.getOrNull(4)?.toIntOrNull()
    )
}