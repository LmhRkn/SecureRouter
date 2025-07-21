package com.tfg.securerouter.data.app.screens.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.home.registry.HomeScreenContentRegistry

/**
 * ViewModel acting as the screen coordinator for the Home feature.
 *
 * Responsibilities:
 * - Manages the loading of all [ScreenComponentModelDefault] modules via [initLoad].
 * - Provides a shared in-memory cache for module data reuse.
 * - Exposes an [isReady] state indicating whether all modules have finished loading.
 *
 * @property sharedCache In-memory cache shared between all modules to prevent redundant operations.
 * @property isReady Indicates whether all modules have successfully loaded their data.
 * @property modules The list of [ScreenComponentModelDefault] modules for the Home screen.
 *
 * @see ScreenCoordinatorDefault
 * @see HomeScreenContentRegistry
 */
class HomeCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = HomeScreenContentRegistry(
        sharedCache
    ).modules

    init {
        viewModelScope.launch { initLoad() }
    }

    /**
     * Initializes all modules by calling their [ScreenComponentModelDefault.loadData] methods.
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
