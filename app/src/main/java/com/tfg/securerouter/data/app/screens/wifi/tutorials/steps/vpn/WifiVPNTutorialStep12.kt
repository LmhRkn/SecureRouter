package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep12(): TutorialStep = TutorialStep(
    title = "Configuración WireGuard pt.1",
    body  = "En el dispositivo que queramos usar para poder configurar el router fuera de al red nos instalamos WireGuard",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec12.jpg"
    )
)
