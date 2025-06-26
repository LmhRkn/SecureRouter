package com.tfg.securerouter.data.menu.screens.home.model.send

import com.tfg.securerouter.data.router.launchCommand

object SendRouterName {
    fun updateRouterAlias(newAlias: String, onResult: (Boolean) -> Unit) {
        val command = """
            uci set wireless.default_radio0.ssid='$newAlias'
            uci commit wireless
            /etc/init.d/network restart
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = onResult
        )
    }
}
