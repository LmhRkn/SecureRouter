package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep3(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 2",
        body  = "Aquí se verán todas las reglas ya creadas",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec3.jpg"
        )
    )
