package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep3(): TutorialStep = TutorialStep(
    title = "Mi ip pública pt.2",
    body  = "Vamos a guardarnos el numero que aparece ahí. Ahora más adelante lo usaremos",
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
    )
)
