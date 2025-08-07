package com.tfg.securerouter.ui.app.screens.wifi.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.ui.app.common.texts.EditableTextField

@Composable
fun DisplayRouterPassword(
    password: String,
    onEditButtonPress: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        Text(text = stringResource(R.string.wifi_password), style = MaterialTheme.typography.bodyLarge)
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditableTextField(
                text = if (isPasswordVisible) password else "●●●●●●●●●●●●",
                textStyle = MaterialTheme.typography.titleMedium,
                onTextSaved = { },
                middleButton = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isPasswordVisible) stringResource(R.string.wifi_hide_password) else stringResource(R.string.wifi_show_password)
                        )
                    }
                },
                buttonSize = 20.dp,
                onEditButtonPress = onEditButtonPress
            )
        }
    }
}
