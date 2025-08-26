package com.tfg.securerouter.data.app.notice.model

import com.tfg.securerouter.data.app.screens.main_screen.model.ScreenEvent

sealed class NoticeEvent : ScreenEvent {
    data class Show(val notice: NoticeSpec) : NoticeEvent()
    data object ClearAll : NoticeEvent()
}
