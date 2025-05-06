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
import com.tfg.securerouter.data.RouterRepository
import com.tfg.securerouter.data.local.database.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tfg.securerouter.ui.router.model.RouterUIModel

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val routerRepository: RouterRepository
) : ViewModel() {

    val uiState: StateFlow<RouterUiState> = routerRepository
        .routers
        .map<List<Router>, RouterUiState> { list ->
            RouterUiState.Success(
                list.map {
                    RouterUIModel(
                        id = it.id,
                        name = it.name,
                        isConnected = it.isConnected,
                        isVpn = it.isVpn,
                        error = it.error
                    )
                }
            )
        }
        .catch { emit(RouterUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RouterUiState.Loading)

    fun addRouter(name: String) {
        viewModelScope.launch {
            routerRepository.add(name)
        }
    }
}

sealed interface RouterUiState {
    object Loading : RouterUiState
    data class Error(val throwable: Throwable) : RouterUiState
    data class Success(val data: List<RouterUIModel>) : RouterUiState
}

