/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tfg.securerouter.ui.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.tfg.securerouter.data.RouterRepository
import com.tfg.securerouter.ui.router.RouterUiState.Error
import com.tfg.securerouter.ui.router.RouterUiState.Loading
import com.tfg.securerouter.ui.router.RouterUiState.Success
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val routerRepository: RouterRepository
) : ViewModel() {

    val uiState: StateFlow<RouterUiState> = routerRepository
        .routers.map<List<String>, RouterUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addRouter(name: String) {
        viewModelScope.launch {
            routerRepository.add(name)
        }
    }
}

sealed interface RouterUiState {
    object Loading : RouterUiState
    data class Error(val throwable: Throwable) : RouterUiState
    data class Success(val data: List<String>) : RouterUiState
}
