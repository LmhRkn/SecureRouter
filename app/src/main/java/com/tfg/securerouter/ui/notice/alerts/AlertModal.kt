package com.tfg.securerouter.ui.notice.alerts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec

@Composable
fun AlertModal(
    spec: AlertSpec,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = { /* Evitamos que se cierre tocando fuera */ }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = spec.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                spec.message?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }

                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (spec.showCancel) {
                        TextButton(onClick = onCancel) {
                            Text(spec.cancelText)
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    Button(onClick = onConfirm) {
                        Text(spec.confirmText)
                    }
                }
            }
        }
    }
}

private fun Modifier.consumeClicks(onClick: () -> Unit) = composed {
    val src = remember { MutableInteractionSource() }
    clickable(interactionSource = src, indication = null) { onClick() }
}
