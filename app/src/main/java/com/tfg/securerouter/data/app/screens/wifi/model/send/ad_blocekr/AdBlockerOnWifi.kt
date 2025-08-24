package com.tfg.securerouter.data.app.screens.wifi.model.send.ad_blocekr

import com.tfg.securerouter.data.app.screens.wifi.model.filter.WifiFilterWebRuleState
import com.tfg.securerouter.data.router.launchCommand


object AdBlockerOnWifi {
    fun adBlockerOnWifi() {
        val command = """
            service adguardhome enable
            service adguardhome start
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { true },
            onResult = {}
        )
    }
}
