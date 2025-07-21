package com.tfg.securerouter.data.app.screens.filter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.filter.registry.FilterScreenContentRegistry
import kotlinx.coroutines.async

/**
 * ViewModel acting as the screen coordinator for the Filter feature.
 *
 * Responsibilities:
 * - Orchestrates the loading of all [ScreenComponentModelDefault] modules via [initLoad].
 * - Maintains a shared in-memory cache for reusing command outputs across modules.
 * - Exposes an [isReady] flag indicating whether all modules have finished loading.
 *
 * @property sharedCache In-memory cache shared between all modules for reusing raw data.
 * @property isReady Indicates whether all modules have successfully loaded their data.
 * @property modules The list of [ScreenComponentModelDefault] modules for the Filter screen.
 *
 * @see ScreenCoordinatorDefault
 * @see FilterScreenContentRegistry
 */
class FilterCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = FilterScreenContentRegistry(sharedCache).modules

    init {
        viewModelScope.launch { initLoad() }
    }

    /**
     * Initializes all modules by triggering their [ScreenComponentModelDefault.loadData] methods.
     *
     * Sets [isReady] to true only if all modules successfully load.
     */
    override suspend fun initLoad() = coroutineScope {
        val results = modules.map { module ->
            async { module.loadData() }
        }.awaitAll()
        _isReady.value = results.all { it }
    }
}
