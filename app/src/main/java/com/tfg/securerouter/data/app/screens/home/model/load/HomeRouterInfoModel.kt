package com.tfg.securerouter.data.app.screens.home.model.load

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.home.state.HomeRouterInfoState
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                _state.value = HomeRouterInfoState(it.routerAlias, it.macAddress, it.wirelessName)
            }
        )
    }

    private fun parseWirelessConfig(output: String): HomeRouterInfoState {
        val ssidRegex = Regex("""^\s*option\s+ssid\s+'([^']+)'""", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
        val standaloneMacRegex = Regex("""^\s*([0-9A-Fa-f]{2}(?::[0-9A-Fa-f]{2}){5})\s*$""", setOf(RegexOption.MULTILINE))
        val optionMacRegex = Regex("""^\s*option\s+macaddr\s+'([0-9A-Fa-f]{2}(?::[0-9A-Fa-f]{2}){5})'""",
            setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
        )

        val router: RouterInfo? = RouterSelectorCache.getRouter(AppSession.routerId.toString())

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

        if (router != null && !ssid.isNullOrBlank() && router.id >= 0 && ssid != router.name) {
            RouterSelectorCache.update(router.id.toString()) { it.copy(name = ssid) }
        }

        return HomeRouterInfoState(
            routerAlias = ssid,
            macAddress = mac.uppercase(),
            wirelessName = wirelessName
        )
    }
}
