package com.tfg.securerouter.data.app.screens.device_manager.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DeviceMAnagerTutorialStep1(): TutorialStep = TutorialStep(
        title = "Pantalla Device Manager",
        body  = "Aquí puedes buscar el dispositivo que quieras modificar.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/DeviceManagerTutorial/Spec1.png"
        )
    )
