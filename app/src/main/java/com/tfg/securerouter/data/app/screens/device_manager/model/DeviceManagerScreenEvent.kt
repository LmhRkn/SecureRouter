package com.tfg.securerouter.data.app.screens.device_manager.model

import com.tfg.securerouter.data.app.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.app.screens.main_screen.model.ScreenEvent

/**
 * Represents all possible UI events for the Device Manager screen.
 *
 * This sealed class defines a closed set of events that the UI or ViewModel
 * can emit to trigger state changes, side-effects, or navigation.
 *
 * Each subclass models a specific type of user interaction or system event.
 *
 * @see ScreenEvent for the common event interface.
 */

sealed class DeviceManagerScreenEvent: ScreenEvent {
    object ToggleSomething : DeviceManagerScreenEvent()
    data class SearchSomething(val query: String) : DeviceManagerScreenEvent()
    data class FilterSomething(val filters: Set<DeviceLabel>) :DeviceManagerScreenEvent()

    data class UpdateData(val payload: String) : DeviceManagerScreenEvent()
}