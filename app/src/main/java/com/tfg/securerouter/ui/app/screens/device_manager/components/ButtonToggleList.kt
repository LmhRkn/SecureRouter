package com.tfg.securerouter.ui.app.screens.device_manager.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.device_manager.model.DeviceManagerScreenEvent
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import com.tfg.securerouter.ui.theme.ExtendedColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable button that toggles between displaying blocked and allowed device lists.
 *
 * Features:
 * - Sends [DeviceManagerScreenEvent.ToggleSomething] to the parent screen’s event bus when clicked.
 * - Dynamically updates its label and background color based on toggle state.
 * - Listens for toggle events from the [ScreenDefault.eventBus] to stay in sync with other components.
 *
 * @param parent The parent [ScreenDefault] providing access to the event bus.
 *
 * @see DeviceManagerScreenEvent.ToggleSomething
 */

@Composable
fun ButtonToggleList(
    parent: ScreenDefault
) {
    var isToggled by rememberSaveable { mutableStateOf(false) }
    val eventFlow = parent.eventBus
    val scope = rememberCoroutineScope()

    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            if (event is DeviceManagerScreenEvent.ToggleSomething) {
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
                scope.launch {
                    parent.trySendEvent(DeviceManagerScreenEvent.ToggleSomething)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isToggled)
                    ExtendedColors.colors.correct.color
                else
                    MaterialTheme.colorScheme.error
            )
        ) {
            val buttonText =
                if (isToggled)
                    stringResource(R.string.dive_manger_show_allowed_devices_button)
                else
                    stringResource(R.string.dive_manger_show_blocked_devices_button)
            Text(buttonText)
        }
    }
}
