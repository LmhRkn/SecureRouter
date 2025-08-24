package com.tfg.securerouter.ui.app.screens.wifi.components.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.app.common.texts.EditableTextField
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiGetVPNQRModel
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiGetWifiQRModel
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.utils.PromptBus
import com.tfg.securerouter.data.utils.QRDecoder
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.flow.first
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DisplayRouterPassword(
    password: String,
    onEditButtonPress: () -> Unit,
    wifiCoordinator: WifiCoordinator
) {
    val scope = rememberCoroutineScope()
    var isPasswordVisible by remember { mutableStateOf(false) }
    val clipboard = LocalClipboardManager.current

    Column {
        Text(text = stringResource(R.string.wifi_password), style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            EditableTextField(
                text = if (isPasswordVisible) password else "●●●●●●●●●●●●",
                textStyle = MaterialTheme.typography.titleMedium,
                onTextSaved = { },
                showEditButton = false,
                actions = {
                    ActionsGrid2x2(
                        visible = isPasswordVisible,
                        onToggle = { isPasswordVisible = !isPasswordVisible },
                        onCopy = { clipboard.setText(AnnotatedString(password)) },
                        onQr = { scope.launch { onQrClick(wifiCoordinator) } }, // 👈 Lanza la suspend
                        onEdit = onEditButtonPress
                    )
                },
                onEditButtonPress = onEditButtonPress
            )
        }
    }
}

suspend fun onQrClick(wifiCoordinator: WifiCoordinator) {
    // Obtiene el modelo
    val qrModel = wifiCoordinator.modules
        .filterIsInstance<WifiGetWifiQRModel>()
        .first()

    // Carga datos (ejecuta el script UCI + qrencode)
    val ok = qrModel.loadData()
    if (!ok) {
        PromptBus.confirm(
            AlertSpec(
                title = "No se pudo generar el QR",
                message = "Fallo al solicitar los datos.",
                confirmText = "Cerrar",
                showCancel = false
            )
        )
        return
    }

    // Espera a que el StateFlow tenga resultado o error (máx. 10s)
    val st = withTimeoutOrNull(10_000) {
        qrModel.state.first { it.error != null || it.qrAnsi.isNotBlank() }
    } ?: run {
        PromptBus.confirm(
            AlertSpec(
                title = "Tiempo de espera agotado",
                message = "No se obtuvo respuesta del generador de QR.",
                confirmText = "Cerrar",
                showCancel = false
            )
        )
        return
    }

    // Muestra resultado
    if (st.error != null) {
        PromptBus.confirm(
            AlertSpec(
                title = "No se pudo generar el QR",
                message = st.error,
                confirmText = "Cerrar",
                showCancel = false
            )
        )
        return
    }

    val pngB64 = QRDecoder.toPngBase64(st.qrAnsi, scale = 10)
    if (pngB64.isEmpty()) {
        PromptBus.confirm(
            AlertSpec(
                title = "Error al convertir el QR",
                message = "Salida ANSI vacía.",
                confirmText = "Cerrar",
                showCancel = false
            )
        )
    } else {
        PromptBus.confirm(
            AlertSpec(
                title = "Escanea este QR para conectarte",
                imageBase64 = pngB64,
                confirmText = "Listo",
                showCancel = false,
                maxHeightDp = 520
            )
        )
    }
}

@Composable
private fun SmallIconButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.width(36.dp), // área táctil compacta
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) { content() }
}

@Composable
private fun ActionsGrid2x2(
    visible: Boolean,
    onToggle: () -> Unit,
    onCopy: () -> Unit,
    onQr: () -> Unit,
    onEdit: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(4.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                SmallIconButton(onToggle) {
                    Icon(
                        imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (visible) stringResource(R.string.wifi_hide_password) else stringResource(R.string.wifi_show_password),
                        modifier = Modifier.width(18.dp)
                    )
                }
                SmallIconButton(onCopy) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", modifier = Modifier.width(18.dp))
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                SmallIconButton(onQr) {
                    Icon(Icons.Default.QrCode, contentDescription = "QR", modifier = Modifier.width(18.dp))
                }
                SmallIconButton(onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.width(18.dp))
                }
            }
        }
    }
}
