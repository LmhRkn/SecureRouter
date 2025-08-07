package com.tfg.securerouter.ui.app.screens.wifi.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.menu_screens.SettingsMenuOption
import com.tfg.securerouter.data.app.menu.menu_screens.WifiMenuOption
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.wifi.utils.validatePassword

@Composable
fun ChangeRouterPassword(
    oldPassword: String,
    onCancel: () -> Unit,
    onPasswordChanged: (String) -> Unit
) {
    val navController = LocalNavController.current

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val oldPasswordCorrect = currentPassword == oldPassword
    val passwordsMatch = newPassword == confirmPassword
    val allFieldsFilled = currentPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank()
    val canSave = oldPasswordCorrect && passwordsMatch && allFieldsFilled

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.wifi_old_password), style = MaterialTheme.typography.bodyLarge)

        PasswordInputField(
            label = stringResource(R.string.wifi_old_password),
            password = currentPassword,
            onPasswordChange = { currentPassword = it },
            showPassword = showCurrentPassword,
            onToggleShowPassword = { showCurrentPassword = !showCurrentPassword },
            validate = { it == oldPassword },
            errorMessage = stringResource(R.string.wifi_wrong_old_password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(stringResource(R.string.wifi_write_new_password), style = MaterialTheme.typography.bodyLarge)

        PasswordInputField(
            label = stringResource(R.string.wifi_new_password),
            password = newPassword,
            onPasswordChange = { newPassword = it },
            showPassword = showNewPassword,
            onToggleShowPassword = { showNewPassword = !showNewPassword },
            validate = { validatePassword(newPassword) == null},
            errorMessage = validatePassword(newPassword)?.let { stringResource(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordInputField(
            label = stringResource(R.string.wifi_repeat_new_password),
            password = confirmPassword,
            onPasswordChange = { confirmPassword = it },
            showPassword = showConfirmPassword,
            onToggleShowPassword = { showConfirmPassword = !showConfirmPassword },
            validate = { it == newPassword },
            errorMessage = stringResource(R.string.wifi_wrong_repeated_password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ChangePasswordActions(
            onCancel = onCancel,
            onSave = {
                onPasswordChanged(newPassword)
                navController.navigate(WifiMenuOption.route) {
                    popUpTo(WifiMenuOption.route) { inclusive = false }
                }
            },
            canSave = canSave
        )
    }
}

@Composable
fun PasswordInputField(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onToggleShowPassword: () -> Unit,
    validate: ((String) -> Boolean)? = null,
    errorMessage: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    var wasFocused by remember { mutableStateOf(false) }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        if (!isFocused && password.isNotBlank()) {
            wasFocused = true
        }
    }

    val isError = validate != null && wasFocused && !validate(password)

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        isError = isError,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        interactionSource = interactionSource,
        trailingIcon = {
            IconButton(onClick = onToggleShowPassword) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (showPassword) stringResource(R.string.wifi_hide_password) else stringResource(R.string.wifi_show_password)
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (isError && errorMessage != null) {
        PasswordErrorText(errorMessage)
    }
}


@Composable
fun PasswordErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun ChangePasswordActions(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    canSave: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = onCancel) {
            Text(stringResource(R.string.cancel_button))
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onSave,
            enabled = canSave
        ) {
            Text(stringResource(R.string.save_button))
        }
    }
}

