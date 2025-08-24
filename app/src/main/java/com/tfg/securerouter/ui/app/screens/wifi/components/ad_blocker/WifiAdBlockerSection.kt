package com.tfg.securerouter.ui.app.screens.wifi.components.ad_blocker

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WifiAdBlockerSection(router: RouterInfo?) {
    val scope = rememberCoroutineScope()
    if (router == null) return

    var checked by remember { mutableStateOf<Boolean?>(null) }
    var out by remember { mutableStateOf<String?>(null) }
    var showConfig by remember { mutableStateOf(false) }
    var runSetup by remember { mutableStateOf(false) }
    var installed by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(router) {
        out = withTimeoutOrNull(5_000) { shUsingLaunch("service adguardhome status") }
        checked = out?.contains("running") == true
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Bloqueador de anuncios:", style = MaterialTheme.typography.bodyLarge)

        ToggleButton(
            checked = checked ?: false,
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
        if (checked == false) {
            AdBlockerOffWifi.adBlockerOffWifi()
        } else if (checked == true && installed == true) {
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
            shUsingLaunch("command -v AdGuardHome >/dev/null 2>&1 && echo AdGuardHome_installed  || echo AdGuardHome_not_installed")
        }

        ok = out?.contains("AdGuardHome_installed")
    }

    when (ok) {
        null -> {}
        true -> {
            Log.d("ConfigAlert", "out: bb")
            onInstalled()
        }
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
