package com.tfg.securerouter.data.menu.screens.device_manager.model

import com.tfg.securerouter.data.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.main_screen.model.ScreenEvent


sealed class DeviceManagerScreenEvent: ScreenEvent{
    object ToggleSomething : DeviceManagerScreenEvent()
    data class SearchSomething(val query: String) : DeviceManagerScreenEvent()
    data class FilterSomething(val filters: Set<DeviceLabel>) : DeviceManagerScreenEvent()

    data class UpdateData(val payload: String) : DeviceManagerScreenEvent()
}