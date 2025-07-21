package com.tfg.securerouter.data.app.screens.language

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.ScreenCoordinatorDefault
import com.tfg.securerouter.data.app.screens.language.registry.LanguageScreenContentRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel acting as the screen coordinator for the Language feature.
 *
 * Responsibilities:
 * - Orchestrates the loading of all [ScreenComponentModelDefault] modules via [initLoad].
 * - Maintains shared UI state for toggling or other screen-wide interactions.
 * - Provides a shared in-memory cache for modules to reuse raw data.
 *
 * @property sharedCache In-memory cache shared between all modules for reusing command outputs.
 * @property isReady Indicates whether all modules have finished loading their data.
 * @property sharedState Mutable toggle state for language-specific UI interactions.
 * @property modules List of [ScreenComponentModelDefault] modules that compose the Language screen.
 *
 * @see ScreenCoordinatorDefault
 * @see LanguageScreenContentRegistry
 */
class LanguageCoordinator : ViewModel(), ScreenCoordinatorDefault {

    private val sharedCache = mutableMapOf<String, Any>()

    private val _isReady = MutableStateFlow(false)
    override val isReady: StateFlow<Boolean> = _isReady

    val modules: List<ScreenComponentModelDefault> = LanguageScreenContentRegistry(sharedCache).modules


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

    private val _sharedState = MutableStateFlow(false)
    val sharedState: StateFlow<Boolean> = _sharedState.asStateFlow()

    /**
     * Toggles the current [sharedState] value between true and false.
     */
    fun toggleSharedState() {
        _sharedState.value = !_sharedState.value
    }

    /**
     * Sets the [sharedState] to a specific value.
     *
     * @param value The new state to set (true or false).
     */
    fun setSharedState(value: Boolean) {
        _sharedState.value = value
    }
}
