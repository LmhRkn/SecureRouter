package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DeviceMAnagerTutorialStep5(): TutorialStep = TutorialStep(
        title = "Pantalla Device Manager",
        body  = "Para volver a Ver los dispositivos no bloqueado pulsa en Mostrar \"Dispositivos Permitidos\".",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/DeviceManagerTutorial/Spec5.jpg"
        )
    )
