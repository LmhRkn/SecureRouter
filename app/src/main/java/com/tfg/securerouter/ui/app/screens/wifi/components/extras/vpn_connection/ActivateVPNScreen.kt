package com.tfg.securerouter.ui.app.screens.wifi.components.extras.vpn_connection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.wifi.model.send.vpn.AddVPNPeer
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import java.net.IDN
import com.tfg.securerouter.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivateVPNScreen(
    onCancel: () -> Unit = {},
    onSave: () -> Unit,
) {
    var nameText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var domainText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val normalizedDomain = remember(domainText.text) { normalizeDomain(domainText.text) }
    val isDomainValid = remember(normalizedDomain) { isValidDomain(normalizedDomain) }
    val normalizedName = remember(nameText.text) { normalizeName(nameText.text) }
    val isNameValid = remember(normalizedName) { isValidName(normalizedName) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(R.string.wifi_vpn_introduce_domain ),
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = nameText,
            onValueChange = { nameText = noWhitespace(it) },
            label = { Text(stringResource(R.string.wifi_vpn_conexion_name)) },
            placeholder = { Text(stringResource(R.string.wifi_vpn_conexion_name_example)) },
            singleLine = true,
            isError = (!isNameValid && normalizedName.isNotBlank()),
            supportingText = {
                when {
                    normalizedName.isBlank() -> Text(stringResource(R.string.wifi_vpn_introduce_a_name))
                    !isNameValid -> Text(stringResource(R.string.wifi_vpn_ino_valid_name))
                    else -> Text("${stringResource(R.string.wifi_url_domain_save_as)} $normalizedName")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = domainText,
            onValueChange = { domainText = noWhitespace(it) },
            label = { Text(stringResource(R.string.wifi_vpn_public_ip)) },
            placeholder = { Text(stringResource(R.string.wifi_vpn_public_ip_example)) },
            singleLine = true,
            isError = (!isDomainValid && normalizedDomain.isNotBlank()),
            supportingText = {
                when {
                    normalizedDomain.isBlank() -> Text(stringResource(R.string.wifi_vpn_introduce_domain_2))
                    !isDomainValid -> Text(stringResource(R.string.wifi_vpn_introduce_domain_2_no_valid))
                    else -> Text("${stringResource(R.string.wifi_url_domain_save_as)} $normalizedDomain")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    AddVPNPeer.addVPNPeer(
                        domain = normalizedDomain,
                        name = normalizedName
                    )
                    AppSession.newDeviceVPN = false
                    RouterSelectorCache.update(AppSession.routerId.toString()) { r -> r.copy(publicIpOrDomain = normalizedDomain) }
                    onSave()
                },
                enabled = isNameValid && isDomainValid
            ) {
                Text(stringResource(R.string.wifi_vpn_activate))
            }

            OutlinedButton(
                onClick = {
                    onCancel()
                }
            ) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    }
}

private fun normalizeDomain(raw: String): String {
    var s = raw.trim().lowercase()
    if (s.isBlank()) return ""
    s = s.replace(Regex("\\s"), "")

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
    if (domain.any { it.isWhitespace() }) return false
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

private fun normalizeName(raw: String): String =
    raw.filterNot(Char::isWhitespace)

private fun isValidName(name: String): Boolean =
    name.isNotBlank() && name.length in 1..64 && name.none(Char::isWhitespace)

private fun noWhitespace(tfv: TextFieldValue): TextFieldValue {
    val cleaned = tfv.text.replace(Regex("\\s"), "")
    return if (cleaned == tfv.text) tfv else TextFieldValue(cleaned, TextRange(cleaned.length))
}