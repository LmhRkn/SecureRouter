package com.tfg.securerouter.ui.app.screens.wifi.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebsRulesState
import com.tfg.securerouter.data.app.screens.wifi.model.time.WifiTimesRulesState
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.wifi.components.extras.filter.WifiFilterWebTable
import com.tfg.securerouter.ui.app.screens.wifi.components.extras.time.WifiTimeTable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiOptionsFilterWeb(wifiFilterWebRule: WifiFilterWebsRulesState) {
    ExpandableSection(title = "Filtros", content = { WifiFilterWebTable(wifiFilterWebRule) })
}