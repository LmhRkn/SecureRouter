package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep11(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.8",
    body  = "Nos apuntamos como queda la url, en este ejemplo es your-url.ddns.net",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec11.jpg"
    )
)
