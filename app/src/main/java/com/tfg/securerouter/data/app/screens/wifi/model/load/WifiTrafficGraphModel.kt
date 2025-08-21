package com.tfg.securerouter.data.app.screens.wifi.model.load

import android.os.Build
import androidx.annotation.RequiresApi
import com.tfg.securerouter.data.app.screens.devices_options.model.time.nextDays
import com.tfg.securerouter.data.app.screens.devices_options.model.time.parseDaysToString
import com.tfg.securerouter.data.app.screens.devices_options.model.time.parseStringToDays
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import com.tfg.securerouter.data.app.screens.wifi.model.WifiTrafficGraphState
import com.tfg.securerouter.data.app.screens.wifi.model.time.WifiTimesRuleState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WifiTrafficGraphModel(
    private val sharedCache: MutableMap<String, Any>
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(WifiTrafficGraphState())
    val state: StateFlow<WifiTrafficGraphState> = _state

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = """
                vnstat -u
                vnstat -h
                vnstat -d
            """.trimIndent(),
            cacheKey = "firewall_rules_raw",
            parse = { output -> WifiTrafficGraphState(output) },
            setState = { rulesState ->
                sharedCache["wifi_time_rule_list"] = rulesState
                _state.value = rulesState
            }
        )
    }
}


