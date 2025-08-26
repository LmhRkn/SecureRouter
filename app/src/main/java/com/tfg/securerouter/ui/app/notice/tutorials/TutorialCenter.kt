package com.tfg.securerouter.ui.app.notice.tutorials

import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object TutorialCenter {
    private val _spec = MutableStateFlow<TutorialSpec?>(null)
    val spec: StateFlow<TutorialSpec?> = _spec

    private val _open = MutableStateFlow(false)
    val open: StateFlow<Boolean> = _open

    fun register(spec: TutorialSpec?) { _spec.value = spec }
    fun open()  { if (_spec.value != null) _open.value = true }
    fun close() { _open.value = false }
    fun hasTutorial(): Boolean = _spec.value != null
}