package com.tfg.securerouter.ui.app.screens.devices_options.components.extras.filter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebRuleState
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebsRulesState
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.toReadableList
import com.tfg.securerouter.data.app.screens.devices_options.model.send.AddFilterWebRuleDevice
import com.tfg.securerouter.data.app.screens.devices_options.model.send.RemoveFilterWebRuleDevice
import com.tfg.securerouter.ui.app.common.tables.GenericRuleTable
import com.tfg.securerouter.ui.app.common.tables.calcNextIndexPreferLocalGeneric
import com.tfg.securerouter.ui.app.common.tables.removeRuleAndShiftGeneric

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceFilterWebTable(
    deviceFilterWebsRule: DeviceFilterWebsRulesState,
    mac: String
) {
    GenericRuleTable<DeviceFilterWebRuleState, DeviceFilterWebsRulesState>(
        container = deviceFilterWebsRule,
        mac = mac,
        rulesOf = { it.rules },
        encode = { r ->
            listOf(
                r.domain,
                r.mac,
                r.index.toString()
            ).joinToString("§")
        },
        decode = { s -> decodeDeviceFilterWebRule(s) },
        macOf = { it.mac },
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
        onRemoveRemote = { r -> RemoveFilterWebRuleDevice.removeFilterWebRuleDevice(r.index) },
        AddOrEdit = { oldRule, mac, nextIdx, current, onSave, onCancel, onBumpToEnd, onRemoveRule ->
            AddDeviceFilterWeb(
                oldRule = oldRule,
                onSave = onSave,
                onBumpToEnd = onBumpToEnd,
                onCancel = onCancel,
                onRemoveRule = onRemoveRule,
                mac = mac,
                nextIndex = nextIdx,
                currentRules = current,
                saveTextButton = if (oldRule != null) stringResource(R.string.edit_button) else stringResource(
                    R.string.add_button
                ),
                explanationText = stringResource(R.string.table_block_device_text),
                onAddRemote = { r ->
                    AddFilterWebRuleDevice.addFilterWebRuleDevice(r)
                }
            )
        }
    )
}

private fun decodeDeviceFilterWebRule(s: String): DeviceFilterWebRuleState {
    val p = s.split('§')
    return DeviceFilterWebRuleState(
        domain = p.getOrNull(0)?.trim().orEmpty(),
        mac = p.getOrNull(0)?.trim().orEmpty(),
        index  = p.getOrNull(1)?.toIntOrNull() ?: -1,
    )
}