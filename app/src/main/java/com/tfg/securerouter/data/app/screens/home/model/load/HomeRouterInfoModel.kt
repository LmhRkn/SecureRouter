package com.tfg.securerouter.data.app.screens.home.model.load

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.home.state.HomeRouterInfoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Model class responsible for loading and managing router configuration information.
 *
 * This implementation of [ScreenComponentModelDefault] fetches and parses the
 * router's wireless configuration from `/etc/config/wireless` and exposes
 * key information such as the router alias (SSID) and MAC address via a [StateFlow].
 *
 * @property sharedCache In-memory cache shared across modules for reusing command outputs.
 * @property state A [StateFlow] exposing the current [HomeRouterInfoState].
 *
 * @see ScreenComponentModelDefault
 * @see HomeRouterInfoState
 */
class HomeRouterInfoModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(HomeRouterInfoState())
    val state: StateFlow<HomeRouterInfoState> = _state

    /**
     * Loads the router’s wireless configuration and updates the state.
     *
     * This method:
     * - Executes `cat /etc/config/wireless` to fetch the raw configuration.
     * - Parses the output to extract the SSID and MAC address.
     * - Stores key data in [sharedCache] for reuse by other modules.
     * - Updates [_state] with a new [HomeRouterInfoState].
     *
     * @return `true` if the data was successfully loaded; `false` otherwise.
     */
    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "cat /etc/config/wireless",
            cacheKey = "wireless_output",
            parse = { parseWirelessConfig(it) },
            setState = {
                sharedCache["router_mac"] = it.macAddress
                sharedCache["router_alias"] = it.routerAlias ?: ""
                _state.value = HomeRouterInfoState(it.routerAlias, it.macAddress)
            }
        )
    }


    /**
     * Parses the wireless configuration output from the router.
     *
     * This method scans the configuration lines to extract:
     * - SSID (used as the router alias)
     * - First MAC address found in the configuration
     *
     * @param output The raw string output from `cat /etc/config/wireless`.
     * @return A [HomeRouterInfoState] containing the extracted data.
     */
    private fun parseWirelessConfig(output: String): HomeRouterInfoState {
        var deviceIp: String? = null
        var ssid: String? = null
        var macAddress: String = ""

        val lines = output.lines()

        for (i in lines.indices) {
            val line = lines[i].trim()
            if (line.startsWith("option ssid")) {
                // Extract SSID (router alias)
                ssid = line.substringAfter("'").substringBefore("'")
            } else if (line.startsWith("list maclist")) {
                // Extract the first MAC address listed
                macAddress = line.substringAfter("'").substringBefore("'")
            }
        }

        return HomeRouterInfoState(
            routerAlias = ssid,
            macAddress = macAddress
        )
    }
}
