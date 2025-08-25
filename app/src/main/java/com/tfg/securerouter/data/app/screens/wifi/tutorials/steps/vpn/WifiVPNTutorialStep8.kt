package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep8(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.5",
    body  = "Pulsaremos ern \"Crear nombre de host\" para crear el dominio",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec8.jpg"
    )
)
