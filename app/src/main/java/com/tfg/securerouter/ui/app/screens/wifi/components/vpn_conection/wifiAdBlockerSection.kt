package com.tfg.securerouter.ui.app.screens.wifi.components.vpn_conection

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.data.automatization.ExecuteAutomationsBlockingUI
import com.tfg.securerouter.data.automatization.registry.AutomatizationRegistryOnSpeedtest
import com.tfg.securerouter.data.automatization.registry.AutomatizationRegistryOnVPN
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.router.shUsingLaunch
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.common.buttons.ToggleButton
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.wifi.components.speedtest.WifiSpeedtest

@Composable
fun WifiVPNSection(
    router: RouterInfo?,
    wifiCoordinator: WifiCoordinator,
) {
    if (router == null) return
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExpandableSection(
        title = "Activar VPN",
        expanded = expanded,
        onExpandedChange = { expanded = it },
        content = {
            ExecuteAutomationsBlockingUI(
                router = RouterSelectorCache.getRouter(AppSession.routerId.toString()),
                factories = AutomatizationRegistryOnVPN.factories,
                sh = ::shUsingLaunch,
                content = {
                    VPNMainScreen(
                        onCancelCollapse = { expanded = false },
                        wifiCoordinator = wifiCoordinator,

                    )
                }
            )
        }
    )
}
