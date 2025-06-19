package com.tfg.securerouter.data.menu.screens.home.model

import com.tfg.securerouter.data.menu.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.menu.screens.home.state.HomeRouterInfoState
import com.tfg.securerouter.data.menu.screens.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeRouterInfoModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(HomeRouterInfoState())
    val state: StateFlow<HomeRouterInfoState> = _state

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
     * @param output The raw string output from `cat /etc/config/wireless`.
     * @return A partial [HomeUiState] containing:
     *   - routerAlias: SSID of the router (nullable)
     *   - routerIp: Identified IP or fallback "Unknown"
     *   - macAddress: First MAC address found
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
