package com.tfg.securerouter.ui.app.screens.sh_login.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.menu_screens.HomeMenuOption
import com.tfg.securerouter.data.app.menu.menu_screens.RouterSelectorMenuOption
import com.tfg.securerouter.data.app.screens.wifi.utils.validatePassword
import com.tfg.securerouter.data.app.automatization.ExecuteAutomationsBlockingUI
import com.tfg.securerouter.data.app.automatization.registry.AutomatizationRegistryBeforeOpening
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.router.shUsingLaunch
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.encryptPassword

import com.tfg.securerouter.ui.app.screens.wifi.components.password.PasswordInputField
import com.tfg.securerouter.ui.app.screens.wifi.components.password.PasswordErrorText

import kotlinx.coroutines.withTimeoutOrNull

@Composable
fun ShLogin(
   navController: NavController
) {
   var loggingIn by rememberSaveable(AppSession.routerId) { mutableStateOf(true) }
   var ok by rememberSaveable(AppSession.routerId) { mutableStateOf(false) }
   var authError by rememberSaveable(AppSession.routerId) { mutableStateOf<String?>(null) }
   val ctx = LocalContext.current

   if (!loggingIn) {
      LaunchedEffect(Unit) {
         val success = try {
            val out = withTimeoutOrNull(5_000) { shUsingLaunch("echo OK") }
            out?.trim()?.endsWith("OK") == true
         } catch (t: Throwable) { false }

         if (success) ok = true
         else {
            authError = ctx.getString(R.string.wifi_wrong_old_password)
            loggingIn = true
         }
      }

      if (ok) {
         ExecuteAutomationsBlockingUI(
            router = RouterSelectorCache.getRouter(AppSession.routerId.toString()),
            factories = AutomatizationRegistryBeforeOpening.factories,
            sh = ::shUsingLaunch,
            content = {
               LaunchedEffect(Unit) {
                  val route = HomeMenuOption.route
                  navController.navigate(route) {
                     popUpTo(route) { inclusive = false }
                  }
               }
            }
         )
      }
   } else {
      login(
         onSave = { loggingIn = false },
         onCancel = {
            val route = RouterSelectorMenuOption.route
            navController.navigate(route) {
               popUpTo(route) { inclusive = false }
            }
         },
         authError = authError,
         onClearError = { authError = null }
      )
   }
}

@Composable
private fun login(
   onSave: () -> Unit,
   onCancel: () -> Unit,
   authError: String?,
   onClearError: () -> Unit
) {
   when (AppSession.createSSHPassword) {
      null -> {
         LaunchedEffect("skipCreateSSH") {
            Log.d("ShLogin", "createSSHPassword == null -> onSave() (LaunchedEffect)")
            onSave()
         }
      }

      true -> {
         var password by rememberSaveable { mutableStateOf("") }
         var confirm by rememberSaveable { mutableStateOf("") }
         var showPassword by rememberSaveable { mutableStateOf(false) }
         var showConfirm by rememberSaveable { mutableStateOf(false) }

         val passwordValid = validatePassword(password) == null
         val confirmValid = confirm.isNotBlank() && confirm == password
         val canPrimary = passwordValid && confirmValid

         Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
         ) {
            Text(
               text = stringResource(R.string.wifi_write_new_password),
               style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(12.dp))

            PasswordInputField(
               label = stringResource(R.string.wifi_new_password),
               password = password,
               onPasswordChange = {
                  password = it
                  if (authError != null) onClearError()
               },
               showPassword = showPassword,
               onToggleShowPassword = { showPassword = !showPassword },
               validate = { validatePassword(password) == null },
               errorMessage = validatePassword(password)?.let { stringResource(it) }
            )

            Spacer(Modifier.height(8.dp))

            PasswordInputField(
               label = stringResource(R.string.wifi_repeat_new_password),
               password = confirm,
               onPasswordChange = {
                  confirm = it
                  if (authError != null) onClearError()
               },
               showPassword = showConfirm,
               onToggleShowPassword = { showConfirm = !showConfirm },
               validate = { it == password },
               errorMessage = stringResource(R.string.wifi_wrong_repeated_password)
            )

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
               TextButton(onClick = onCancel) { Text(stringResource(R.string.cancel_button)) }
               Spacer(Modifier.height(0.dp))
               Button(
                  onClick = {
                     RouterSelectorCache.update(AppSession.routerId.toString()) { r ->
                        r.copy(sshPassword = encryptPassword(password))
                     }
                     onSave()
                  },
                  enabled = canPrimary
               ) { Text("Crear") }
            }
         }
      }

      else -> {
         var password by rememberSaveable { mutableStateOf("") }
         var showPassword by rememberSaveable { mutableStateOf(false) }
         val canPrimary = password.isNotBlank()

         Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
         ) {
            Text(
               text = stringResource(R.string.wifi_old_password),
               style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(12.dp))

            PasswordInputField(
               label = stringResource(R.string.wifi_old_password),
               password = password,
               onPasswordChange = {
                  password = it
                  if (authError != null) onClearError()
               },
               showPassword = showPassword,
               onToggleShowPassword = { showPassword = !showPassword },
               validate = null,
               errorMessage = null
            )

            if (authError != null) {
               Spacer(Modifier.height(8.dp))
               PasswordErrorText(message = authError)
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
               TextButton(onClick = onCancel) { Text(stringResource(R.string.cancel_button)) }
               Button(
                  onClick = {
                     RouterSelectorCache.update(AppSession.routerId.toString()) { r ->
                        r.copy(sshPassword = encryptPassword(password))
                     }
                     onSave()
                  },
                  enabled = canPrimary
               ) { Text("Conectar") }
            }
         }
      }
   }
}
