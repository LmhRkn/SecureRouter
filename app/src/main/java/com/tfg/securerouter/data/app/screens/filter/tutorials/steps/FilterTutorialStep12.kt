package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep12(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 3",
        body  = "Si queremos bloquear el acceso a todos los dispositivos a una web, pulsamos en el botón \"Añadir regla\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec12.jpg"
        )
    )
