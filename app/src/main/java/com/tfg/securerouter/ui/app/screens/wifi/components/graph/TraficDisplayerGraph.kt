package com.tfg.securerouter.ui.app.screens.wifi.components.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.tfg.securerouter.data.app.screens.wifi.model.WifiTrafficGraphState
import com.tfg.securerouter.data.app.screens.wifi.utils.graph.VnstatMonitorScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrafficDisplayerGraph(trafficData: WifiTrafficGraphState) {
    val latest by rememberUpdatedState(trafficData.trafficData)
    val provider: suspend () -> String = { latest }
    VnstatMonitorScreen(provider = provider)
}
