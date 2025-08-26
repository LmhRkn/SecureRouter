package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep17(): TutorialStep = TutorialStep(
    title = "Configuración Wireguard pt.2",
    body  = "Pulsaremos en \"Escanear desde código QR\" y escaneamos el código QR genrado por la aplicación",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec17.jpg"
    )
)
