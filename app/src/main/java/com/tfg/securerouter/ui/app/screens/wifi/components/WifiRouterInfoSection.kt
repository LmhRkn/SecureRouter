package com.tfg.securerouter.ui.app.screens.wifi.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Router
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.app.common.texts.EditableTextField
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import com.tfg.securerouter.data.app.screens.home.model.send.SendRouterName
import com.tfg.securerouter.data.app.screens.wifi.model.WifiRouterInfoState
import com.tfg.securerouter.ui.app.common.router_info.RouterInfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiRouterInfoSection(
    state: WifiRouterInfoState
) {
    RouterInfoSection(
        alias = state.routerAlias,
        mac = state.macAddress
    )
}