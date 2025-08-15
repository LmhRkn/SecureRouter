package com.tfg.securerouter.data.app.screens.home.model.send

import com.tfg.securerouter.data.router.launchCommand

/**
 * Utility object for sending configuration commands to the router.
 *
 * This object encapsulates commands that modify the router’s configuration,
 * providing a clean API for higher-level components to use.
 *
 * Currently supports updating the router’s wireless SSID (alias).
 *
 * @see launchCommand
 */
object SendRouterName {
    /**
     * Updates the router’s wireless SSID (alias) to [newAlias].
     *
     * This function:
     * - Constructs and sends the following commands to the router:
     *   1. `uci set wireless.default_radio0.ssid='<newAlias>'`
     *   2. `uci commit wireless`
     *   3. `/etc/init.d/network restart`
     * - Uses [launchCommand] to execute the commands asynchronously.
     * - Parses the command output and invokes [onResult] with `true` if the output is non-blank.
     *
     * @param newAlias The new SSID (alias) to set for the router.
     * @param onResult Callback invoked with the result of the operation:
     *   - `true` if the command executed successfully.
     *   - `false` if an error occurred.
     *
     * @see launchCommand
     */
    fun updateRouterAlias(wirelessName: String, newAlias: String) {
        val command = """
            rm -f /tmp/dhcp.leases
            uci set wireless.$wirelessName.ssid='$newAlias'
            uci commit wireless
            /etc/init.d/network restart
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
