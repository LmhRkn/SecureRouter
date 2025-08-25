package com.tfg.securerouter.ui.app.screens.home.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.home.state.HomeRouterInfoState
import com.tfg.securerouter.ui.app.common.router_info.RouterInfoSection
import com.tfg.securerouter.ui.app.common.texts.EditableTextField

/**
 * Composable that displays the router's information section on the Home screen.
 *
 * Features:
 * - Shows the router alias (SSID) or falls back to the MAC address if alias is null.
 * - Allows editing of the alias using [EditableTextField].
 * - Displays the router’s MAC address below the editable field.
 *
 * @param state The [HomeRouterInfoState] containing the router alias and MAC address.
 * @param onEditAliasClick Callback invoked when the user saves a new alias.
 *
 * @see EditableTextField
 */
@Composable
fun HomeRouterInfoSection(
    state: HomeRouterInfoState,
) {
    RouterInfoSection(
        alias = state.routerAlias,
        mac = state.macAddress
    )
}