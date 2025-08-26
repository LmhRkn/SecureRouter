package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep22(): TutorialStep = TutorialStep(
    title = "Más funcionalidades de la VPN pt.2",
    body  = "O puedes borrar un punto de acceso ya creado para que no pueda segir entrando.",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec22.jpg"
    )
)
