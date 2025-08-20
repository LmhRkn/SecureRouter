// ui/app/screens/wifi/components/TraficDisplayerGraph.kt
package com.tfg.securerouter.ui.app.screens.wifi.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.tfg.securerouter.data.app.screens.wifi.model.WifiTraficGraphState
import com.tfg.securerouter.data.utils.graph.VnstatMonitorScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TraficDisplayerGraph(traficdata: WifiTraficGraphState) {

    val latest by rememberUpdatedState(traficdata.traficData)
    val provider: suspend () -> String = { latest }
    VnstatMonitorScreen(provider = provider)
}
