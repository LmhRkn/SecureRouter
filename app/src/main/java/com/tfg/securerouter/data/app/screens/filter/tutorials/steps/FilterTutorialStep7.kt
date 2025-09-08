package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep7(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 6",
        body  = "Y añadimos la regla con el botón \"Añadir\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec7.jpg"
        )
    )
