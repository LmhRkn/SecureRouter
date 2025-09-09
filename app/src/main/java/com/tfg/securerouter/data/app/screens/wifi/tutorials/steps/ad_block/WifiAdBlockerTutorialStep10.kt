package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiAdBlockerTutorialStep10(): TutorialStep = TutorialStep(
    title = R.string.wifi_adblocker_tutorial_title,
    body  = R.string.wifi_adblocker_tutorial_step10,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec10.jpg"
    )
)
