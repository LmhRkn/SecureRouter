package com.tfg.securerouter.ui.app.common.router_info

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.home.model.send.SendRouterName
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.ui.app.common.texts.EditableTextField
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.notice.alerts.AlertModal

@Composable
fun RouterInfoSection(
    alias: String?,
    mac: String,
    modifier: Modifier = Modifier
) {
    var showAlert by remember { mutableStateOf(false) }
    var pending by remember { mutableStateOf<String?>(null) }

    EditableTextField(
        text = alias ?: mac,
        onTextSaved = { newAlias ->
            val trimmed = newAlias.trim()
            if (trimmed.isNotEmpty() && trimmed != alias) {
                pending = trimmed
                showAlert = true
            }
        },
        modifier = modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        mac,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground
    )

    if (showAlert) {
        AlertModal(
            spec = confirmSpec,
            onConfirm = {
                pending?.let({
                    if (!pending.isNullOrEmpty()) {
                        RouterSelectorCache.update(AppSession.routerId.toString()) { r ->
                            r.copy(name = pending!!)
                        }
                        SendRouterName.updateRouterAlias(AppSession.wirelessName ?: "", pending!!)
                    }
                })
                showAlert = false
                pending = null
            },
            onCancel = {
                showAlert = false
                pending = null
            }
        )
    }
}

val confirmSpec: AlertSpec = AlertSpec(
    title = "¿Aplicar cambios?",
    message = "Esta acción reiniciará el router. ¿Continuar?",
    confirmText = "Aceptar",
    cancelText = "Cancelar",
    showCancel = true
)