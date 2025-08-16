package com.tfg.securerouter.ui.app.screens.devices_options.components.extras.time

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.app.common.buttons.WeekdaySelector
import com.tfg.securerouter.ui.app.common.texts.TimePickerField
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.devices_options.model.time.DeviceTimesRuleState
import com.tfg.securerouter.data.app.screens.devices_options.model.time.toReadableList
import com.tfg.securerouter.data.app.screens.devices_options.model.send.AddTimeRuleDevice
import com.tfg.securerouter.ui.app.common.tables.saveRuleGeneric

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddDeviceTime(
    oldRule: DeviceTimesRuleState? = null,
    onSave: (DeviceTimesRuleState) -> Unit,
    onBumpToEnd: (String) -> Int,
    onCancel: () -> Unit,
    onRemoveRule: (DeviceTimesRuleState) -> Unit = {},
    mac: String,
    nextIndex: Int = -1,
    currentRules: List<DeviceTimesRuleState>,
    saveTextButton: String = stringResource(R.string.add_button),
    cancelTextButton: String = stringResource(R.string.cancel_button),
    explanationText: String
) {
    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm:ss") }

    val localTimeSaver = remember {
        Saver<LocalTime, String>(
            save = { it.format(timeFmt) },
            restore = { LocalTime.parse(it, timeFmt) }
        )
    }

    val initialStart: LocalTime =
        oldRule?.start?.takeIf { it.isNotBlank() }?.let { LocalTime.parse(it, timeFmt) }
            ?: LocalTime.of(8, 0)

    val initialFinish: LocalTime =
        oldRule?.finish?.takeIf { it.isNotBlank() }?.let { LocalTime.parse(it, timeFmt) }
            ?: LocalTime.of(20, 0)

    var startTime by rememberSaveable(stateSaver = localTimeSaver) { mutableStateOf(initialStart) }
    var endTime   by rememberSaveable(stateSaver = localTimeSaver) { mutableStateOf(initialFinish) }

    val initialDays: Set<String> =
        oldRule?.days
            ?.split(",", " ")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toSet()
            ?: emptySet()

    var selectedDays by remember { mutableStateOf(initialDays) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            "$explanationText $mac",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.table_to_hour_text))
            Spacer(modifier = Modifier.width(8.dp))
            TimePickerField(time = startTime) { startTime = it }

            Spacer(modifier = Modifier.width(16.dp))

            Text(stringResource(R.string.table_from_hour_text))
            Spacer(modifier = Modifier.width(8.dp))
            TimePickerField(time = endTime) { endTime = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.table_days_of_the_week_text))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeekdaySelector(
                selectedDays = selectedDays,
                onDayToggle = { day ->
                    selectedDays = if (selectedDays.contains(day)) {
                        selectedDays - day
                    } else {
                        selectedDays + day
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val indexSubtraction = if (oldRule?.index2 != null) 2 else if (oldRule != null) 1 else 0

            Button(
                enabled = selectedDays.isNotEmpty(),
                onClick = {
                    val orderedDays = listOf("L","M","X","J","V","S","D").filter { it in selectedDays }
                    val startStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    val finishStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    val crossesMidnight = endTime.isBefore(startTime)

                    val previewLabel = DeviceTimesRuleState(
                        index = -1, index2 = null,
                        start = startStr, finish = finishStr,
                        days = orderedDays.joinToString(","), mac = mac
                    ).toReadableList()

                    saveRuleGeneric(
                        previewLabel = previewLabel,
                        crossesMidnight = crossesMidnight,
                        oldRule = oldRule,
                        currentRules = currentRules,
                        nextIndex = nextIndex,
                        onBumpToEnd = onBumpToEnd,
                        onRemoveRule = onRemoveRule,
                        indexOf = { it.index },
                        index2Of = { it.index2 },
                        labelOf = { it.toReadableList() },
                        buildRule = { idx, idx2 ->
                            DeviceTimesRuleState(
                                index  = idx,
                                index2 = idx2,
                                start  = startStr,
                                finish = finishStr,
                                days   = orderedDays.joinToString(","),
                                mac    = mac
                            )
                        },
                        onAddRemote = { r ->
                            AddTimeRuleDevice.addTimeRuleDevice(r)
                        },
                        onSaveLocal = onSave
                    )
                }
            ) { Text(saveTextButton) }


            OutlinedButton(onClick = { onCancel() }) {
                Text(cancelTextButton)
            }
        }
    }
}