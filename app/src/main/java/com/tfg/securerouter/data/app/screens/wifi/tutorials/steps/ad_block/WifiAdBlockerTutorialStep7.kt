package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiAdBlockerTutorialStep7(): TutorialStep = TutorialStep(
        title = "Configuración adblocke pt.5",
        body  = "Crea un usuario y contraseña de minimo 8 caracteres",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec7.jpg"
        )
    )
