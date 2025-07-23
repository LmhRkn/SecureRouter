package com.tfg.securerouter.data.app.screens.language.model

import com.tfg.securerouter.data.app.screens.device_manager.model.DeviceManagerScreenEvent
import com.tfg.securerouter.data.app.screens.main_screen.model.ScreenEvent

sealed class LanguageScreenEvent: ScreenEvent {
    data class LanguageSelected(val query: String) : LanguageScreenEvent()

    data class UpdateData(val payload: String) : LanguageScreenEvent()
}