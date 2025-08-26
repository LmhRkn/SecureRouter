package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep15(): TutorialStep = TutorialStep(
    title = "Creación punto de acceso VPN pt.3",
    body  = "Usaremos este QR para establecer la conexión usando la aplicacion instalada WireGuard.\n\nSi vas a poner la VPN en el mismo dispositivo, haz una captura de pantalla",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec15.jpg"
    )
)
