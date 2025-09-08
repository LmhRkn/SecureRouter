package com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DevicesOptionTutorialStep7(): TutorialStep = TutorialStep(
        title = "Bloqueo dispositivos por tiempo pt. 1",
        body = "Si quieres hacer que un dispositivo no pueda usar internet durante un periodo de tiempo concreto y unos días concretos de la semana tienes que pulsar sobre el botón + de tiempos.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
        )
    )
