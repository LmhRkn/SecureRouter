package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiAdBlockerTutorialStep6(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.4",
        body  = "Baja y pulsa \"Next\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec6.jpg"
        )
    )
