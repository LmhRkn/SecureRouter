package com.tfg.securerouter.ui.app.screens.devices_options.components.extras.time

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.devices_options.model.time.DeviceTimesRuleState
import com.tfg.securerouter.data.app.screens.devices_options.model.time.DeviceTimesRulesState
import com.tfg.securerouter.data.app.screens.devices_options.model.time.toReadableList
import com.tfg.securerouter.data.app.screens.devices_options.model.send.RemoveTimeRuleDevice
import com.tfg.securerouter.ui.app.common.tables.GenericRuleTable
import com.tfg.securerouter.ui.app.common.tables.calcNextIndexPreferLocalGeneric
import com.tfg.securerouter.ui.app.common.tables.removeRuleAndShiftGeneric

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceTimeTable(
    deviceTimesRule: DeviceTimesRulesState,
    mac: String
) {
    GenericRuleTable<DeviceTimesRuleState, DeviceTimesRulesState>(
        container = deviceTimesRule,
        mac = mac,
        rulesOf = { it.rules },
        encode = { r ->
            listOf(
                r.start, r.finish, r.days, r.mac,
                r.index.toString(), r.index2?.toString().orEmpty()
            ).joinToString("§")
        },
        decode = { s -> decodeDeviceRule(s) },
        macOf = { it.mac },
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
        onRemoveRemote = { r -> RemoveTimeRuleDevice.removeTimeRuleDevice(r.index, r.index2) },
        AddOrEdit = { oldRule, mac, nextIdx, current, onSave, onCancel, onBumpToEnd, onRemoveRule ->
            AddDeviceTime(
                oldRule = oldRule,
                onSave = onSave,
                onBumpToEnd = onBumpToEnd,
                onCancel = onCancel,
                onRemoveRule = onRemoveRule,
                mac = mac,
                nextIndex = nextIdx,
                currentRules = current,
                saveTextButton = if (oldRule != null) stringResource(R.string.edit_button) else stringResource(R.string.add_button),
                explanationText = stringResource(R.string.table_block_device_text)
            )
        }
    )
}

private fun decodeDeviceRule(s: String): DeviceTimesRuleState {
    val p = s.split('§')
    return DeviceTimesRuleState(
        start  = p.getOrNull(0).orEmpty(),
        finish = p.getOrNull(1).orEmpty(),
        days   = p.getOrNull(2).orEmpty(),
        mac    = p.getOrNull(3).orEmpty(),
        index  = p.getOrNull(4)?.toIntOrNull() ?: -1,
        index2 = p.getOrNull(5)?.toIntOrNull()
    )
}