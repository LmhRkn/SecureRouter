package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep5(): TutorialStep = TutorialStep(
    title = "Configuración No-Ip pt.2",
    body  = "Crearemos nuestra dando a \"Inscribir\" y creando nuestra cuenta, es importante gardarnos usuario y contraeña",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec5.jpg"
    )
)
