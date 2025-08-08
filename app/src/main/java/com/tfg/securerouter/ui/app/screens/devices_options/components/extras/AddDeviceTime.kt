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
import com.tfg.securerouter.data.app.common.screen_components.rule_table.RuleTableModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddDeviceTime(
    onSave: (RuleTableModel) -> Unit,
    onCancel: () -> Unit
) {
    var startTime by rememberSaveable  { mutableStateOf(LocalTime.of(8, 0)) }
    var endTime by rememberSaveable  { mutableStateOf(LocalTime.of(20, 0)) }
    var selectedDays by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.wifi_block_device_text), style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.wifi_from_hour_text))
            Spacer(modifier = Modifier.width(8.dp))
            TimePickerField(time = startTime) { startTime = it }

            Spacer(modifier = Modifier.width(16.dp))

            Text(stringResource(R.string.wifi_to_hour_text))
            Spacer(modifier = Modifier.width(8.dp))
            TimePickerField(time = endTime) { endTime = it }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.wifi_days_of_the_week_text))

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
                val days = selectedDays.joinToString(",")
                val ruleText = "Bloquear los $days de ${startTime.format(DateTimeFormatter.ofPattern("HH:mm"))} a ${endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}h"
                onSave(RuleTableModel(ruleText))
            }) {
                Text(stringResource(R.string.add_button))
            }

            OutlinedButton(onClick = {  onCancel() }) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    }
}

