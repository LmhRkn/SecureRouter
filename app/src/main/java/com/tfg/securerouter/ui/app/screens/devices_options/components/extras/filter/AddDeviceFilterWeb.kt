package com.tfg.securerouter.ui.app.screens.devices_options.components.extras.filter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.devices_options.model.filter.DeviceFilterWebRuleState
import com.tfg.securerouter.ui.app.common.tables.saveRuleGeneric
import java.net.IDN

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddDeviceFilterWeb(
    oldRule: DeviceFilterWebRuleState? = null,
    onSave: (DeviceFilterWebRuleState) -> Unit,
    onBumpToEnd: (String) -> Int,
    onCancel: () -> Unit,
    onRemoveRule: (DeviceFilterWebRuleState) -> Unit = {},
    mac: String,
    nextIndex: Int = -1,
    currentRules: List<DeviceFilterWebRuleState>,
    saveTextButton: String,
    cancelTextButton: String = stringResource(R.string.cancel_button),
    explanationText: String,
    onAddRemote: (DeviceFilterWebRuleState) -> Unit = {}
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(oldRule?.domain.orEmpty()))
    }

    val normalized = remember(text.text) { normalizeDomain(text.text) }
    val isValid = remember(normalized) { isValidDomain(normalized) }
    val duplicate = remember(normalized, currentRules, oldRule) {
        normalized.isNotBlank() &&
                currentRules.any { it.domain.equals(normalized, ignoreCase = true) } &&
                !oldRule?.domain.equals(normalized, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(explanationText, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Dominio o URL") },
            placeholder = { Text("ej.: youtube.com o https://sub.ejemplo.com/path") },
            singleLine = true,
            isError = (!isValid && normalized.isNotBlank()) || duplicate,
            supportingText = {
                when {
                    normalized.isBlank() -> Text("Introduce un dominio o URL")
                    !isValid -> Text("Dominio no válido")
                    duplicate -> Text("Ya existe una regla para $normalized")
                    else -> Text("Se guardará como: $normalized")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                enabled = isValid && !duplicate,
                onClick = {
                    val previewLabel = normalized // tu label es el dominio
                    saveRuleGeneric(
                        previewLabel = previewLabel,
                        crossesMidnight = false, // no aplica aquí
                        oldRule = oldRule,
                        currentRules = currentRules,
                        nextIndex = nextIndex,
                        onBumpToEnd = onBumpToEnd,
                        onRemoveRule = onRemoveRule,
                        indexOf = { it.index },
                        index2Of = { null },
                        labelOf = { it.domain },
                        buildRule = { idx, _ ->
                            DeviceFilterWebRuleState(
                                index = idx,
                                mac = mac,
                                domain = normalized
                            )
                        },
                        onAddRemote = { r ->
                            onAddRemote(r)  // engánchalo a tu capa UCI si quieres
                        },
                        onSaveLocal = onSave
                    )
                }
            ) { Text(saveTextButton) }

            OutlinedButton(onClick = onCancel) {
                Text(cancelTextButton)
            }
        }
    }
}

private fun normalizeDomain(raw: String): String {
    var s = raw.trim().lowercase()
    if (s.isBlank()) return ""
    s = s.removePrefix("http://")
        .removePrefix("https://")
        .removePrefix("ftp://")
    val at = s.indexOf('@')
    if (at != -1) s = s.substring(at + 1)
    s = s.substringBefore('/').substringBefore('?').substringBefore('#').substringBefore(':')
    if (s.startsWith("www.")) s = s.removePrefix("www.")
    return try { IDN.toASCII(s) } catch (_: Exception) { s }
}

private fun isValidDomain(domain: String): Boolean {
    if (domain.isBlank() || domain.length > 253) return false
    val labels = domain.split('.')
    if (labels.size < 2) return false
    return labels.all { label ->
        label.isNotBlank() &&
                label.length in 1..63 &&
                label.first().isLetterOrDigit() &&
                label.last().isLetterOrDigit() &&
                label.all { it.isLetterOrDigit() || it == '-' }
    }
}
