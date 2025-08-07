package com.tfg.securerouter.ui.app.common.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeekdaySelector(
    selectedDays: Set<String>,
    onDayToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf("L", "M", "X", "J", "V", "S", "D")
    Row(modifier = modifier) {
        days.forEach { day ->
            val selected = day in selectedDays
            Button(
                onClick = { onDayToggle(day) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp),
                modifier = Modifier
                    .height(32.dp)
                    .defaultMinSize(minWidth = 32.dp)
            ) {
                Text(
                    text = day,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
        }
    }
}
