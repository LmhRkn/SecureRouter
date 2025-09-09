package com.tfg.securerouter.ui.app.screens.devices_options.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.devices_options.model.time.DeviceTimesRulesState
import com.tfg.securerouter.ui.app.common.texts.ExpandableSection
import com.tfg.securerouter.ui.app.screens.devices_options.components.extras.time.DeviceTimeTable

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeviceOptionsTimes(deviceTimesRule: DeviceTimesRulesState, mac: String) {
    ExpandableSection(title = stringResource(R.string.device_option_times_label), content = { DeviceTimeTable(deviceTimesRule, mac) })
}