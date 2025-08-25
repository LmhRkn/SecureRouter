package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiAdBlockerTutorialStep11(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.9",
        body  = "Pulsa en el \"DNS settings\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec11.jpg"
        )
    )
