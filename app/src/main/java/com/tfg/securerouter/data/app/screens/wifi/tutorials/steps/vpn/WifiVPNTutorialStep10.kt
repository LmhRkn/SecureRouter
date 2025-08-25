package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep10(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.7",
    body  = "Y pulsamos en \"Crear\"",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec10.jpg"
    )
)
