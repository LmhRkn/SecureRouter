package com.tfg.securerouter.data.app.screens.devices_options.alerts

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.alerts.AlertSpec

fun BlockDeviceAlert(): AlertSpec = AlertSpec(
        title = R.string.device_option_block_device_question,
        message = R.string.device_option_block_device_warning,
        confirmText = R.string.block_button,
        showCancel = true
    )
