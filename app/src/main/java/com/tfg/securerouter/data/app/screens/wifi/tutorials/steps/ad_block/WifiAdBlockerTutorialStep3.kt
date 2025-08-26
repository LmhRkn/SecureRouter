package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiAdBlockerTutorialStep3(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.1",
        body  = "Si es la primera vez que encienden el adblocker, escriba lo siguiente en el buscador: 192.168.10.1:3000",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec3.jpg"
        )
    )
