package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiTutorialStep5(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.3",
        body  = "Cambia el puerto de 3000 a 8080",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec5.jpg"
        )
    )
