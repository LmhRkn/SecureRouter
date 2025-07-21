package com.tfg.securerouter.data.app.screens.wifi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.wifi.registry.WifiContentRegistry
import kotlinx.coroutines.async

/**
 * ViewModel acting as the screen coordinator for the Wi-Fi feature.
 *
 * Responsibilities:
 * - Orchestrates the loading of all [ScreenComponentModelDefault] modules via [initLoad].
 * - Maintains a shared in-memory cache for modules to reuse raw data or command outputs.
 * - Exposes an [isReady] state flag indicating whether all modules have finished loading.
 *
 * @property sharedCache In-memory cache shared between all modules for data reuse.
 * @property isReady Indicates whether all modules have successfully loaded their data.
 * @property modules List of [ScreenComponentModelDefault] modules that compose the Wi-Fi screen.
 *
 * @see ScreenCoordinatorDefault
 * @see WifiContentRegistry
 */
class WifiCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = WifiContentRegistry(
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
