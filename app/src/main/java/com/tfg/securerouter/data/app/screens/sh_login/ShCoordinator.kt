package com.tfg.securerouter.data.app.screens.sh_login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.sh_login.registry.ShLoginScreenContentRegistry
import kotlinx.coroutines.async

class ShLoginCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = ShLoginScreenContentRegistry(sharedCache).modules


    init {
        viewModelScope.launch { initLoad() }
    }

    /**
     * Initializes all modules by triggering their [ScreenComponentModelDefault.loadData] methods.
     *
     * Sets [isReady] to true only if all modules load successfully.
     */
    override suspend fun initLoad() = coroutineScope {
        val results = modules.map { module ->
            async { module.loadData() }
        }.awaitAll()
        _isReady.value = results.all { it }
    }
}
