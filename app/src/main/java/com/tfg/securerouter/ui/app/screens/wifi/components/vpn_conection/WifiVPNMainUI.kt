package com.tfg.securerouter.ui.app.screens.wifi.components.vpn_conection

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tfg.securerouter.data.app.screens.wifi.WifiCoordinator
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.ui.app.screens.wifi.components.extras.vpn_connection.ActivateVPNScreen
import com.tfg.securerouter.ui.app.screens.wifi.components.extras.vpn_connection.DeviceManagementScreen
import com.tfg.securerouter.ui.app.screens.wifi.components.extras.vpn_connection.ShowQRAlert


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VPNMainScreen(
    onCancelCollapse: () -> Unit = {},
    wifiCoordinator: WifiCoordinator,
) {
    var newDeviceVPN by remember { mutableStateOf<Int>(if (AppSession.newDeviceVPN) 0 else 1) }
    if (newDeviceVPN == 0) {
        ActivateVPNScreen(
            onCancel = onCancelCollapse,
            onSave = { newDeviceVPN = 2 }
        )
    } else if (newDeviceVPN == 1){
        DeviceManagementScreen(
            onAddDevice = {
                newDeviceVPN = 0
            },
            wifiCoordinator = wifiCoordinator
        )
    } else {
        ShowQRAlert(
            wifiCoordinator,
            onQrReaded = {
                newDeviceVPN = 1
            }
        )
    }
}
