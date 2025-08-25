package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep6(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.3",
    body  = "Una vez dentro de la cuenta, pulsaremos ern \"DDNS y acceso rapido\"",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec6.jpg"
    )
)
