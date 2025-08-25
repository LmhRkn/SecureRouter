package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep13(): TutorialStep = TutorialStep(
    title = "Creación punto de acceso VPN pt.1",
    body  = "Abrimos las opciones de VPN",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec13.jpg"
    )
)
