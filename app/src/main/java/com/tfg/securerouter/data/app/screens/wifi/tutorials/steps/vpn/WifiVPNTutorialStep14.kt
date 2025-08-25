package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep14(): TutorialStep = TutorialStep(
    title = "Creación punto de acceso VPN pt.2",
    body  = "Si es la primera vez, directamente podremos añadir un punto de acceso. Le ponderemos el nombre que queramos y la url que hemos creado en No-Ip y pulsamos en Activar VPN",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec14.jpg"
    )
)
