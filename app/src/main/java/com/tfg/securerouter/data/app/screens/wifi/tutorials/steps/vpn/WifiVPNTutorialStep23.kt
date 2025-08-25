package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep23(): TutorialStep = TutorialStep(
    title = "Más funcionalidades de la VPN pt.3",
    body  = "Si pulsas sobre el punto de acceso que quieres borrar.",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec23.jpg"
    )
)
