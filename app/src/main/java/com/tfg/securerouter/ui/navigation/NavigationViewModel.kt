package com.tfg.securerouter.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.local.preferences.LanguagePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val languagePreferences: LanguagePreferences
) : ViewModel() {

    private val _startDestination = MutableStateFlow("loading")
    val startDestination: StateFlow<String> = _startDestination

    fun determineStartDestination() {
        viewModelScope.launch {
            val lang = languagePreferences.getSelectedLanguage()
            _startDestination.value = if (lang == null) "language" else "main"
        }
    }
}
