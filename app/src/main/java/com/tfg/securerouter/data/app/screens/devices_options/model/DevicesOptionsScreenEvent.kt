package com.tfg.securerouter.data.app.screens.devices_options.model

import com.tfg.securerouter.data.app.screens.main_screen.model.ScreenEvent

sealed class DevicesOptionsScreenEvent: ScreenEvent {
    data class DevicesOptionsSelected(val query: String) : DevicesOptionsScreenEvent()

    data class UpdateData(val payload: String) : DevicesOptionsScreenEvent()
}