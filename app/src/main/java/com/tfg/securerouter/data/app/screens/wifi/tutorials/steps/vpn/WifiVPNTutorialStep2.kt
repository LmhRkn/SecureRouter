package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep2(): TutorialStep = TutorialStep(
    title = "Mi ip pública pt.1",
    body  = "Lo primero que vamos a hacer es ver cual es nuestra ip publica, para ello vamos a entrar en www.cual-es-mi-ip.net",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec2.jpg"
    )
)
