package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep24(): TutorialStep = TutorialStep(
    title = "Más funcionalidades de la VPN pt.3",
    body  = "Y pulsas en \"Borrar\" podrás borrar dicho punto de acceso.",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec24.jpg"
    )
)
