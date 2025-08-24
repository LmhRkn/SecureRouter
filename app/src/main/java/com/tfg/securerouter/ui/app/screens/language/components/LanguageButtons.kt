package com.tfg.securerouter.ui.app.screens.language.components

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.menu.menu_screens.SettingsMenuOption
import com.tfg.securerouter.data.app.navegation.LocalNavController
import com.tfg.securerouter.data.app.screens.language.model.LanguageScreenEvent
import com.tfg.securerouter.data.app.screens.language.utils.setDeviceLanguage
import com.tfg.securerouter.ui.app.screens.ScreenDefault
import kotlinx.coroutines.flow.filterIsInstance

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun LanguageButtons(
    parent: ScreenDefault
) {
    val navController = LocalNavController.current
    val context = LocalContext.current

    var selectedLanguage by rememberSaveable { mutableStateOf<String?>(null) }
    val eventFlow = parent.eventBus

    LaunchedEffect(parent) {
        parent.eventBus
            .filterIsInstance<LanguageScreenEvent.LanguageSelected>()
            .collect { ev ->
                selectedLanguage = ev.query
                Log.d("LanguageButtons", "Se puede pulsar")
            }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                navegateToSettings(navController)
            }
        ) {
            Text(stringResource(R.string.language_back_button))
        }

        Button(
            onClick = {
                setDeviceLanguage(context, selectedLanguage)

                val sharedPreferences =
                    context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
                sharedPreferences.edit {
                    putString("language", selectedLanguage)
                    putBoolean("navigate_to_settings", true)
                }

                navegateToSettings(navController)
            },
            enabled = selectedLanguage != null
        ) {
            Text(stringResource(R.string.language_apply_button))
        }
    }
}

private fun navegateToSettings(navController: NavController) {
    navController.navigate(SettingsMenuOption.route) {
        popUpTo(SettingsMenuOption.route) { inclusive = false }
    }
}

