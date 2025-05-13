package com.tfg.securerouter.data.model

import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tfg.securerouter.data.state.TopBarModel
import androidx.compose.runtime.State

class TopBarViewModel : ViewModel() {
    private val _topBarState = mutableStateOf(TopBarModel())
    val topBarState: State<TopBarModel> = _topBarState

    fun updateTitle(newTitle: String) {
        _topBarState.value = _topBarState.value.copy(title = newTitle)
    }

    fun updateRouterConnected(connected: Boolean) {
        _topBarState.value = _topBarState.value.copy(routerConnected = connected)
    }

    fun updateVpnConnected(connected: Boolean) {
        _topBarState.value = _topBarState.value.copy(vpnConnected = connected)
    }
}
