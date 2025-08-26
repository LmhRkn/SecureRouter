package com.tfg.securerouter.ui.app.screens.wifi.components.extras.vpn_connection

import androidx.compose.runtime.*
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.data.app.screens.wifi.model.load.WifiGetVPNQRModel
import com.tfg.securerouter.data.app.notice.utils.PromptBus
import com.tfg.securerouter.data.app.notice.model.alerts.AlertSpec
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Modifier
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import com.tfg.securerouter.data.utils.QRDecoder

@Composable
fun ShowQRAlert(
    wifiCoordinator: WifiCoordinator,
    onQrReaded: () -> Unit,
) {
    val qrModel = remember(wifiCoordinator) {
        wifiCoordinator.modules.filterIsInstance<WifiGetVPNQRModel>().first()
    }

    var isLoading by remember { mutableStateOf(true) }
    var shownOnce by remember { mutableStateOf(false) }

    LaunchedEffect(qrModel) {
        isLoading = true
        coroutineScope {
            val wait = async { delay(3_000) }
            wait.await()
            val load = async { qrModel.loadData() }
            load.await()
        }
        isLoading = false
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
    }

    val state by qrModel.state.collectAsState()

    LaunchedEffect(isLoading, state.confText, state.qrPngBase64, state.error) {
        if (isLoading || shownOnce) return@LaunchedEffect

        when {
            state.error != null -> { /* igual que antes */ }

            state.confText.isNotBlank() -> {
                val pngB64 = QRDecoder.toPngBase64(state.confText, scale = 10)
                if (pngB64.isNotEmpty()) {
                    val ok = PromptBus.confirm(
                        AlertSpec(
                            title = "Escanea este QR con el nuevo dispositivo",
                            imageBase64 = pngB64,
                            confirmText = "Listo",
                            showCancel = false,
                            maxHeightDp = 520
                        )
                    )
                    if (ok) {
                        onQrReaded()
                    }
                    shownOnce = true
                } else {
                    PromptBus.confirm(
                        AlertSpec(
                            title = "Error al convertir el QR",
                            message = "Salida ANSI vacía.",
                            confirmText = "Cerrar",
                            showCancel = false
                        )
                    )
                    shownOnce = true
                }
            }
        }
    }
}
