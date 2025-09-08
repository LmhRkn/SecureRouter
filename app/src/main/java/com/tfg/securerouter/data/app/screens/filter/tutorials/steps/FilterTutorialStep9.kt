package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep9(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 8",
        body  = "Si queremos quitar la norma pulsamos en la \"x\" situada a la derecha de dicha norma.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec9.jpg"
        )
    )
