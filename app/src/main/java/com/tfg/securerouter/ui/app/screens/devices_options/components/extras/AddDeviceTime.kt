package com.tfg.securerouter.ui.app.screens.devices_options.components.extras

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.app.common.buttons.WeekdaySelector
import com.tfg.securerouter.ui.app.common.texts.TimePickerField
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceTimesRuleState
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toReadableList
import com.tfg.securerouter.data.app.common.screen_components.rule_table.RuleTableModel
import com.tfg.securerouter.data.app.screens.devices_options.model.send.AddTimeRuleDevice
import kotlin.text.format

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddDeviceTime(
    onSave: (RuleTableModel) -> Unit,
    onBumpToEnd: (String) -> Unit,
    onCancel: () -> Unit,
    mac: String,
    nextIndex: Int,
    currentRules: List<RuleTableModel>
) {
    var startTime by rememberSaveable { mutableStateOf(LocalTime.of(8, 0)) }
    var endTime by rememberSaveable { mutableStateOf(LocalTime.of(20, 0)) }
    var selectedDays by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            stringResource(R.string.table_block_device_text),
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
            Button(onClick = {
                val orderedDays = listOf("L","M","X","J","V","S","D").filter { it in selectedDays }
                val startStr = startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                val finishStr = endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                val crossesMidnight = endTime.isBefore(startTime)

                val newRule = DeviceTimesRuleState(
                    index = nextIndex,
                    index2 = if (crossesMidnight) nextIndex + 1 else null,
                    start = startStr,
                    finish = finishStr,
                    days = orderedDays.joinToString(","),
                    mac = mac
                )
                val newTitle = newRule.toReadableList()

                val exists = currentRules.any { it.title.trim() == newTitle.trim() }

                if (exists) {
                    onBumpToEnd(newTitle)
                } else {
                    onSave(RuleTableModel(newTitle, nextIndex, newRule.index2))
                    AddTimeRuleDevice.addTimeRuleDevice(newRule)
                }
            }) {
                Text(stringResource(R.string.add_button))
            }


            OutlinedButton(onClick = { onCancel() }) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    }
}

