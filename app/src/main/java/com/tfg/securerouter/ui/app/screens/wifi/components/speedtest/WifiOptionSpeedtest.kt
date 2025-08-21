package com.tfg.securerouter.ui.app.screens.wifi.components.speedtest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.tfg.securerouter.data.automatization.ExecuteAutomationsBlockingUI
import com.tfg.securerouter.data.automatization.registry.AutomatizationRegistryOnSpeedtest
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.router.shUsingLaunch
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiOptionSpeedtest() {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExpandableSection(
        title = "Speedtest",
        expanded = expanded,
        onExpandedChange = { expanded = it },
        content = {
            ExecuteAutomationsBlockingUI(
                router = RouterSelectorCache.getRouter(AppSession.routerId.toString()),
                factories = AutomatizationRegistryOnSpeedtest.factories,
                sh = ::shUsingLaunch,
                content = {
                    if (!AppSession.cancelledSpeedTestByUser) {
                        WifiSpeedtest()
                    } else {
                        LaunchedEffect(Unit) {
                            expanded = false
                            AppSession.cancelledSpeedTestByUser = false
                        }
                    }
                }
            )
        }
    )
}
