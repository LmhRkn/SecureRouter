package com.tfg.securerouter.data.notice.model

import com.tfg.securerouter.data.app.screens.main_screen.model.ScreenEvent
import com.tfg.securerouter.ui.notice.NoticeSpec

sealed class NoticeEvent : ScreenEvent {
    data class Show(val notice: NoticeSpec) : NoticeEvent()
    data object ClearAll : NoticeEvent()
}
