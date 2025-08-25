package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep16(): TutorialStep = TutorialStep(
    title = "Configuración Wireguard pt.1",
    body  = "Pulsaremos en el botón \"+\" para agregar la conexión",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec16.jpg"
    )
)
