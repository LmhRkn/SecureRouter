package com.tfg.securerouter.data.app.screens.main_screen.model

/**
 * Marker interface for UI events across different screens.
 *
 * All screen-specific event types (such as user actions, state changes, or side effects)
 * should implement this interface. It allows ViewModels and UI layers to work
 * with a common event type while still supporting screen-specific behavior
 * through sealed class implementations.
 */
interface ScreenEvent

