package com.tfg.securerouter.data.app.screens.devices_options.model.send

import com.tfg.securerouter.data.router.launchCommand


object BlockDevice {
    fun blockDevice(mac: String) {
        val command = """
            uci set wireless.@wifi-iface[0].macfilter='deny'
            uci add_list wireless.@wifi-iface[0].maclist='$mac'
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
