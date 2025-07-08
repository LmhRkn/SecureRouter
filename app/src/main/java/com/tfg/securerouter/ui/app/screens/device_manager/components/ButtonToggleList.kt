package com.tfg.securerouter.ui.app.screens.device_manager.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent

import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.theme.LocalExtraColors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun ButtonToggleList(
    parent: ScreenDefault
) {
    var isToggled by remember { mutableStateOf(false) }
    val eventFlow = parent.eventBus
    val extraColors = LocalExtraColors.current

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            if (event is com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent.ToggleSomething) {
                isToggled = !isToggled
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    parent.sendEvent(com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent.ToggleSomething)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isToggled) extraColors.errorStatus else MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Enviar evento")
        }
    }
}
