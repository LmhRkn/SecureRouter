package com.tfg.securerouter.ui.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.local.preferences.LanguagePreferences
import com.tfg.securerouter.ui.language.state.LanguageUiState
import com.tfg.securerouter.ui.language.util.supportedLanguages
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languagePreferences: LanguagePreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow<LanguageUiState>(LanguageUiState.Loading)
    val uiState: StateFlow<LanguageUiState> = _uiState

    init {
        loadLanguages()
    }

    private fun loadLanguages() {
        val savedLang = languagePreferences.getSelectedLanguage()
        _uiState.value = LanguageUiState.Success(supportedLanguages, savedLang)
    }

    fun selectLanguage(code: String) {
        viewModelScope.launch {
            languagePreferences.saveLanguage(code)
            loadLanguages()
        }
    }
}
