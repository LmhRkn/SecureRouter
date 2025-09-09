package com.tfg.securerouter.data.app.screens.devices_options.alerts

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.alerts.AlertSpec

fun UnblockDeviceAlert(): AlertSpec = AlertSpec(
        title = R.string.device_option_unblock_device_question,
        message = R.string.device_option_unblock_device_info,
        confirmText = R.string.unblock_button,
        showCancel = true
)
