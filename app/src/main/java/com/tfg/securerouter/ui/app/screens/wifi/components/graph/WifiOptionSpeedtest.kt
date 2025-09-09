package com.tfg.securerouter.ui.app.screens.wifi.components.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.wifi.model.WifiTrafficGraphState
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiOptionTrafficFilterWeb(wifiTrafficGraph: WifiTrafficGraphState) {
    ExpandableSection(title = stringResource(R.string.wifi_traffic_graph), content = { TrafficDisplayerGraph(wifiTrafficGraph) })
}