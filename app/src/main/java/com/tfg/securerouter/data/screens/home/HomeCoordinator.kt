package com.tfg.securerouter.data.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.screens.home.registry.HomeScreenContentRegistry
import kotlinx.coroutines.async

class HomeCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = com.tfg.securerouter.data.screens.home.registry.HomeScreenContentRegistry(
        sharedCache
    ).modules

    init {
        viewModelScope.launch { initLoad() }
    }

    override suspend fun initLoad() = coroutineScope {
        val results = modules.map { module ->
            async { module.loadData() }
        }.awaitAll()
        _isReady.value = results.all { it }
    }
}
