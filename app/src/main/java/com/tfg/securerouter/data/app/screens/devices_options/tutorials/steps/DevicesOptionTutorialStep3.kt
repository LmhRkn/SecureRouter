package com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DevicesOptionTutorialStep3(): TutorialStep = TutorialStep(
        title = "Tipo dispositivo",
        body  = "Si el tipo de dispositivo no se ha detectado bien, puedes cambiarlo dando al botón de editar sobre el icono de dispositivo.\n" +
                "Este cambio solo afecta a tu aplicación.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
        )
    )
