package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep21(): TutorialStep = TutorialStep(
    title = "Más funcionalidades de la VPN pt.1",
    body  = "Si ya tienes al menos un punto de acceso, te saldrá la opción de agregar otro.",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec21.jpg"
    )
)
