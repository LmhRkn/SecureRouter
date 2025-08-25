package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep4(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.1",
    body  = "Ahora entraremos a www.noip.com para poder configurar el \"camino\" a nuestro router, no os preocupeis que es seguro",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec4.jpg"
    )
)
