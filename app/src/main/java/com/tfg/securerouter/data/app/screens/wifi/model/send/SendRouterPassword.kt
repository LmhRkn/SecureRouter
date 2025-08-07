package com.tfg.securerouter.data.app.screens.wifi.model.send

import com.tfg.securerouter.data.router.launchCommand

object SendRouterPassword {
    fun updateRouterAlias(wirelessName: String, newPassword: String) {
        val command = """
            uci set wireless.$wirelessName.key='$newPassword'
            uci commit wireless
            wifi reload
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
