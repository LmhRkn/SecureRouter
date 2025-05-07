package com.tfg.securerouter.ui.language.state

import com.tfg.securerouter.ui.language.model.LanguageUIModel

sealed class LanguageUiState{
    object Loading : LanguageUiState()
    data class Success(val languages: List<LanguageUIModel>, val selectedCode: String?) : LanguageUiState()
    data class Error(val message: String) : LanguageUiState()
}