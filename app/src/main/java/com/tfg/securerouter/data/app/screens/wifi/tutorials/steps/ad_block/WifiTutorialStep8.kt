package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiTutorialStep8(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.6",
        body  = "Pulsa en el menu",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec8.jpg"
        )
    )
