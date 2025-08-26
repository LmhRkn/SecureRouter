package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep20(): TutorialStep = TutorialStep(
    title = "Configuración Wireguard pt.5",
    body  = "Con la VPN activa y desde una red distinta a la de casa. Si entramos a la aplicación veremos que está conectado a través de la VPN",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec20.jpg"
    )
)
