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
    onCancel: () -> Unit,
    onDismissRequest: (() -> Unit)? = onCancel
) {
    Box(Modifier.fillMaxSize()) {
        val scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f)
        Box(
            Modifier
                .fillMaxSize()
                .consumeClicks { onDismissRequest?.invoke() }
        ) {
            Canvas(Modifier.matchParentSize()) {
                drawRect(scrimColor)
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .align(Alignment.Center),
            shape = RoundedCornerShape(18.dp),
            tonalElevation = 8.dp
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    spec.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                spec.message?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (spec.showCancel) {
                        TextButton(onClick = onCancel) { Text(spec.cancelText) }
                        Spacer(Modifier.width(8.dp))
                    }
                    Button(onClick = onConfirm) { Text(spec.confirmText) }
                }
            }
        }
    }
}

private fun Modifier.consumeClicks(onClick: () -> Unit) = composed {
    val src = remember { MutableInteractionSource() }
    clickable(interactionSource = src, indication = null) { onClick() }
}
