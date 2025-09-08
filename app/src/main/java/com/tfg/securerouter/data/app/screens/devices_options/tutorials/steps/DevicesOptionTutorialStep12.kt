package com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DevicesOptionTutorialStep12(): TutorialStep = TutorialStep(
        title = "Bloqueo dispositivo pt. 1",
        body = "Para impedir el acceso al wifi, pulsas en el botón al final de la página \"Bloquear Dispositivo\".",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
        )
    )
