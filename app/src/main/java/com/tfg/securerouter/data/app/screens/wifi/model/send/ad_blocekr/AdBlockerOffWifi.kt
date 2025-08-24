package com.tfg.securerouter.data.app.screens.wifi.model.send.ad_blocekr

import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand


object AdBlockerOffWifi {
    fun adBlockerOffWifi() {
        val command = """
            service adguardhome stop
            service adguardhome disable
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { true },
            onResult = {}
        )
    }
}
