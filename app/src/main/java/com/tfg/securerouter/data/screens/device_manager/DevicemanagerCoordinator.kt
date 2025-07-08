package com.tfg.securerouter.data.screens.device_manager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.screens.device_manager.registry.AdministrarDispositivosScreenContentRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.asStateFlow

class DevicemanagerCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = AdministrarDispositivosScreenContentRegistry(sharedCache).modules

    init {
        viewModelScope.launch { initLoad() }
    }

    override suspend fun initLoad() = coroutineScope {
        val results = modules.map { module ->
            async { module.loadData() }
        }.awaitAll()
        _isReady.value = results.all { it }
    }

    // Toogle between allowed and blocked dives list
    private val _sharedState = MutableStateFlow(false)
    val sharedState: StateFlow<Boolean> = _sharedState.asStateFlow()

    fun toggleSharedState() {
        _sharedState.value = !_sharedState.value
    }

    fun setSharedState(value: Boolean) {
        _sharedState.value = value
    }
}
