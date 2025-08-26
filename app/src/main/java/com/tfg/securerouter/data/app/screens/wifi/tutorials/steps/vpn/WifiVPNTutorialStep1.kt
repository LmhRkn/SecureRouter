package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep1(): TutorialStep = TutorialStep(
    title = "Configuración VPN",
    body  = "Si quieres poder configurar tu red desde fuera de casa, es importante configurar esta función.",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec1.png"
    )
)
