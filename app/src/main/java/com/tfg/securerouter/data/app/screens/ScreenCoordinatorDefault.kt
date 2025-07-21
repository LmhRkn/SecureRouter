package com.tfg.securerouter.data.app.screens

import kotlinx.coroutines.flow.StateFlow

/**
 * Base interface for all screen coordinators in the app.
 *
 * A screen coordinator acts as a ViewModel that:
 * - Orchestrates the initialization and loading of all [ScreenComponentModelDefault] modules
 *   composing a screen.
 * - Maintains shared UI state (e.g., whether modules have finished loading).
 * - Provides a consistent API for the UI layer to observe readiness and trigger loading.
 *
 * Implementations of this interface (e.g., [DeviceManagerCoordinator], [HomeCoordinator]) ensure
 * that their respective screens are fully prepared before rendering UI components.
 *
 * @property isReady A [StateFlow] indicating whether all modules have successfully loaded their data.
 *
 * @see ScreenComponentModelDefault
 */
interface ScreenCoordinatorDefault {
    val isReady: StateFlow<Boolean>

    /**
     * Initializes and loads all data modules for the screen.
     *
     * Implementations should launch asynchronous tasks (e.g., network calls, cache loading)
     * and update [isReady] accordingly.
     */
    suspend fun initLoad()
}
