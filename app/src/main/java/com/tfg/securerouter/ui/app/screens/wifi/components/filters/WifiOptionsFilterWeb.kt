package com.tfg.securerouter.ui.app.screens.wifi.components.filters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebsRulesState
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.wifi.components.extras.filter.WifiFilterWebTable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiOptionsFilterWeb(wifiFilterWebRule: WifiFilterWebsRulesState) {
    ExpandableSection(title = "Filtros", content = { WifiFilterWebTable(wifiFilterWebRule) })
}