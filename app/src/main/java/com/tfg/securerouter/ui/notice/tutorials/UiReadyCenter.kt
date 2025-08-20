package com.tfg.securerouter.ui.notice.tutorials

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UiReadyCenter {
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready
    fun setReady(isReady: Boolean) { _ready.value = isReady }
}