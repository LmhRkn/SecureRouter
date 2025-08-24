package com.tfg.securerouter.ui.notice.alerts

import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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

// 👇 IMPORTS NUEVOS
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.asImageBitmap
import android.util.Base64

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

                spec.imageBase64?.let { b64 ->
                    val bitmap = remember(b64) {
                        runCatching {
                            val bytes = Base64.decode(b64, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        }.getOrNull()
                    }
                    if (bitmap != null) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = spec.maxHeightDp.dp)
                                .padding(top = 8.dp, bottom = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    } else {
                        Text("No se pudo decodificar el QR.", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(Modifier.height(16.dp))
                spec.message?.takeIf { it.isNotBlank() }?.let { msg ->
                    val scroll = rememberScrollState()
                    val base = MaterialTheme.typography.bodyLarge
                    val size = (base.fontSize.value * spec.fontScale).sp

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = spec.maxHeightDp.dp)
                            .padding(vertical = 8.dp)
                            .verticalScroll(scroll)
                    ) {
                        Text(
                            text = msg,
                            style = base.copy(
                                fontSize = size,
                                fontFamily = if (spec.isMonospace) FontFamily.Monospace else base.fontFamily,
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                lineHeightStyle = LineHeightStyle(
                                    alignment = LineHeightStyle.Alignment.Center,
                                    trim = LineHeightStyle.Trim.None
                                ),
                            ),
                            softWrap = spec.softWrap,
                            overflow = TextOverflow.Clip,
                            // maxLines = Int.MAX_VALUE // opcional
                        )
                    }
                }

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
