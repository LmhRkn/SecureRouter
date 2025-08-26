package com.tfg.securerouter.data.app.screens.wifi.model.load

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.home.model.load.HomeRouterInfoState
import com.tfg.securerouter.data.app.screens.wifi.model.WifiRouterInfoState
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
class WifiRouterInfoModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(WifiRouterInfoState())
    val state: StateFlow<WifiRouterInfoState> = _state

    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = """
                cat /etc/config/wireless
                cat /sys/class/net/br-lan/address
                uci show wireless | grep '.ssid=' | cut -d'.' -f2 | head -n 1
            """.trimIndent(),
            cacheKey = "wireless_output",
            parse = { parseWirelessConfig(it) },
            setState = {
                sharedCache["router_mac"] = it.macAddress
                sharedCache["router_alias"] = it.routerAlias ?: ""
                sharedCache["router_wireless_name"] = it.wirelessName
                sharedCache["router_password"] = it.password
                _state.value = WifiRouterInfoState(it.routerAlias, it.macAddress, it.wirelessName, it.password)
            }
        )
    }

    private fun parseWirelessConfig(output: String): WifiRouterInfoState {
        val ssidRegex = Regex("""^\s*option\s+ssid\s+'([^']+)'""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        val standaloneMacRegex = Regex("""^\s*([0-9A-Fa-f]{2}(?::[0-9A-Fa-f]{2}){5})\s*$""", setOf(RegexOption.MULTILINE))
        val optionMacRegex = Regex("""^\s*option\s+macaddr\s+'([0-9A-Fa-f]{2}(?::[0-9A-Fa-f]{2}){5})'""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
        )

        val ssid = ssidRegex.findAll(output).lastOrNull()?.groupValues?.get(1)

        val mac = standaloneMacRegex.findAll(output).lastOrNull()?.groupValues?.get(1)
            ?: optionMacRegex.findAll(output).lastOrNull()?.groupValues?.get(1)
            ?: ""

        val wirelessNameRegex = Regex(
            """(@wifi-iface\[\d+\]|default_radio\d+)""",
            RegexOption.IGNORE_CASE
        )
        val wirelessName = wirelessNameRegex.findAll(output)
            .map { it.value }
            .lastOrNull()
            ?: ""

        val keyRegex = Regex("""^\s*option\s+key\s+'([^']+)'""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        val password = keyRegex.findAll(output).lastOrNull()?.groupValues?.get(1) ?: ""

        return WifiRouterInfoState(
            routerAlias = ssid,
            macAddress = mac.uppercase(),
            wirelessName = wirelessName,
            password = password
        )
    }

}
