package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DeviceMAnagerTutorialStep5(): TutorialStep = TutorialStep(
        title = R.string.device_manager_screen,
        body  = R.string.device_manager_spec_5,
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/DeviceManagerTutorial/Spec5.jpg"
        )
    )
