package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DeviceMAnagerTutorialStep4(): TutorialStep = TutorialStep(
        title = "Pantalla Device Manager",
        body  = "Si quieres ver los dispositivos bloqueados pulsas en \"Mostrar Dispositivos Bloqueados\".",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/DeviceManagerTutorial/Spec4.jpg"
        )
    )
