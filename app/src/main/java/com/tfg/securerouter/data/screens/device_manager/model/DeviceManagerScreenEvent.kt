package com.tfg.securerouter.data.screens.device_manager.model

import com.tfg.securerouter.data.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.main_screen.model.ScreenEvent


sealed class DeviceManagerScreenEvent: ScreenEvent{
    object ToggleSomething : com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent()
    data class SearchSomething(val query: String) : com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent()
    data class FilterSomething(val filters: Set<DeviceLabel>) : com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent()

    data class UpdateData(val payload: String) : com.tfg.securerouter.data.screens.device_manager.model.DeviceManagerScreenEvent()
}