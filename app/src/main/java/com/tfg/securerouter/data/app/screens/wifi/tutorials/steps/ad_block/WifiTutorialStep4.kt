package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiTutorialStep4(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.2",
        body  = "Una vez dentro, pulsa en \"Get Started\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec4.jpg"
        )
    )
