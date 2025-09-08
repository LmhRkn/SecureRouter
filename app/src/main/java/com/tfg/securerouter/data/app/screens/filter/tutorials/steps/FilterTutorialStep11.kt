package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep11(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 2",
        body  = "Aquí se verán todas las reglas ya creadas",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec11.jpg"
        )
    )
