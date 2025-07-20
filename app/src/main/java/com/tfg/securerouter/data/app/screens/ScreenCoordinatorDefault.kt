package com.tfg.securerouter.data.app.screens

import kotlinx.coroutines.flow.StateFlow

interface ScreenCoordinatorDefault {
    val isReady: StateFlow<Boolean>
    suspend fun initLoad()
}
