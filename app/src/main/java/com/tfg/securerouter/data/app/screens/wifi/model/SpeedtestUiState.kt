package com.tfg.securerouter.data.app.screens.wifi.model

sealed interface SpeedtestUiState {
    data object Idle : SpeedtestUiState
    data object Running : SpeedtestUiState
    data class Result(val data: SpeedData) : SpeedtestUiState
    data class Error(val message: String) : SpeedtestUiState
}

data class SpeedData(
    val downloadMbps: Double?,
    val uploadMbps: Double?,
    val idleLatencyMs: Double?,
    val jitterMs: Double?,
    val packetLossPct: Double?,
    val server: String?,
    val isp: String?,
    val resultUrl: String?
)