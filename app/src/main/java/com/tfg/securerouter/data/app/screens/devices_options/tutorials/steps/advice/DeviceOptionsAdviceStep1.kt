package com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.advice

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DeviceOptionsAdviceStep1(): TutorialStep = TutorialStep(
        title = "IMPORTANTE",
        body  = "Si no reconoces un dispositivo, es importante bloquearlo.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/Global/Alert.png"
        )
    )
