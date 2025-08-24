package com.tfg.securerouter.ui.app.screens.wifi.components.ad_blocker

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.screens.wifi.model.send.ad_blocekr.AdBlockerOffWifi
import com.tfg.securerouter.data.app.screens.wifi.model.send.ad_blocekr.AdBlockerOnWifi
import com.tfg.securerouter.data.automatization.ExecuteAutomationsBlockingUI
import com.tfg.securerouter.data.automatization.registry.AutomatizationRegistryBeforeAdBlocker
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.router.shUsingLaunch
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.common.buttons.ToggleButton
import com.tfg.securerouter.ui.notice.alerts.AlertModal
import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun WifiAdBlockerSection(router: RouterInfo?) {
    if (router == null) return

    var checked by remember { mutableStateOf(false) }
    var showConfig by remember { mutableStateOf(false) }
    var runSetup by remember { mutableStateOf(false) }
    var installed by remember { mutableStateOf<Boolean?>(null) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Bloqueador de anuncios:", style = MaterialTheme.typography.bodyLarge)

        ToggleButton(
            checked = checked,
            onCheckedChange = { nuevo ->
                checked = nuevo
                if (nuevo) {
                    showConfig = true
                } else {
                    installed = null
                    runSetup = false
                }
            }
        )
    }

    if (showConfig) {
        ConfigAlert(
            onConfirm = {
                showConfig = false
                runSetup = true
            },
            onCancel = {
                showConfig = false
                checked = false
                installed = null
                runSetup = false
            },
            onInstalled = {
                showConfig = false
                installed = true
                runSetup = true
            }
        )
    }

    if (runSetup) {
        ExecuteAutomationsBlockingUI(
            router = RouterSelectorCache.getRouter(AppSession.routerId.toString()),
            factories = AutomatizationRegistryBeforeAdBlocker.factories,
            sh = ::shUsingLaunch,
            content = {
                installed = true
            }
        )
    }

    LaunchedEffect(checked, installed) {
        if (!checked) {
            AdBlockerOffWifi.adBlockerOffWifi()
        } else if (installed == true) {
            AdBlockerOnWifi.adBlockerOnWifi()
        }
    }
}

@Composable
fun ConfigAlert(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onInstalled: () -> Unit,
) {
    var ok by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        val out = withTimeoutOrNull(5_000) {
            shUsingLaunch("command -v AdGuardHome >/dev/null 2>&1 && echo 1 || echo 0")
        }

        ok = out?.contains('1')
    }

    when (ok) {
        null -> {}
        true -> onInstalled()
        false -> {
            Log.d("ConfigAlert", "out: aa")

            val alert = remember {
                AlertSpec(
                    title = "Requiere instalación",
                    message = "Esta acción requiere configuración\n en ${AppSession.routerIp}:3000.\nMira la guía.",
                    confirmText = "Continuar",
                    cancelText = "Cancelar",
                    showCancel = true
                )
            }
            AlertModal(
                spec = alert,
                onConfirm = onConfirm,
                onCancel = onCancel
            )
        }
    }
}
