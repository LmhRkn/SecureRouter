package com.tfg.securerouter.data.app.screens.device_manager.tutorials.steps.advice

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DeviceManagerAdviceStep2(): TutorialStep = TutorialStep(
        title = R.string.tutorial_important_label,
        body  = R.string.device_manager_hint_2,
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/Global/Alert.png"
        )
    )
