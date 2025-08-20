package com.tfg.securerouter.ui.app.screens.wifi.components.password

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.tfg.securerouter.data.app.screens.wifi.model.WifiRouterInfoState
import com.tfg.securerouter.data.app.screens.wifi.model.send.SendRouterPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WifiRouterPassword(
    state: WifiRouterInfoState
) {
    var changeRouterPassword by remember { mutableStateOf(false) }

    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(changeRouterPassword) {
            ChangeRouterPassword(
                oldPassword = state.password,
                onCancel = { changeRouterPassword = !changeRouterPassword },
                onPasswordChanged =  { newPassword ->
                    SendRouterPassword.updateRouterAlias( wirelessName = state.wirelessName, newPassword = newPassword)
                })
        } else DisplayRouterPassword(password = state.password, onEditButtonPress = { changeRouterPassword = !changeRouterPassword })
        Spacer(modifier = Modifier.width(8.dp))
    }
}