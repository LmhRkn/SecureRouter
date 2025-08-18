package com.tfg.securerouter.data.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo

object AppSession {
    private val _currentRouter = MutableStateFlow<RouterInfo?>(null)
    val currentRouter = _currentRouter.asStateFlow()

    fun setCurrentRouter(router: RouterInfo?) {
        _currentRouter.value = router
    }

    @Volatile var routerId: Int? = null
    @Volatile var routerIp: String? = null
    @Volatile var packageInstaller: String? = null
}