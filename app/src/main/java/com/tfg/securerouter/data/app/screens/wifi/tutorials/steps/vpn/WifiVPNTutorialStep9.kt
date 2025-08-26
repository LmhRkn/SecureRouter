package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep9(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.6",
    body  = "Ponemos el nombre que queramos q se vea en nuestro url (\"camino\") por ejemplo: your-url",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec9.jpg"
    )
)
