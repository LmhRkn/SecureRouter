package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiTutorialStep1(): TutorialStep = TutorialStep(
    title = "Configuración Adblocker",
    body  = "Si quieres poder navegar sin anuncios, puedes activar esta función...\nAdemás, ...",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/AdBlock/Spec1.png"
    )
)
