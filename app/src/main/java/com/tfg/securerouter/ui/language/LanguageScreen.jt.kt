package com.tfg.securerouter.ui.language

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.ui.language.components.LanguageCard
import com.tfg.securerouter.ui.language.state.LanguageUiState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LanguageScreen(onLanguageSelected: () -> Unit) {
    val viewModel: LanguageViewModel = hiltViewModel()
    val state: LanguageUiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (state) {
        is LanguageUiState.Loading -> CircularProgressIndicator()
        is LanguageUiState.Error -> Text("Error cargando idiomas")
        is LanguageUiState.Success -> {
            val data = state as LanguageUiState.Success
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background) // o cualquier color
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Selecciona un idioma", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                data.languages.forEach { lang ->
                    LanguageCard(language = lang) {
                        viewModel.selectLanguage(lang.code)
                        (context as Activity).recreate()
                    }
                }
            }
        }
    }
}