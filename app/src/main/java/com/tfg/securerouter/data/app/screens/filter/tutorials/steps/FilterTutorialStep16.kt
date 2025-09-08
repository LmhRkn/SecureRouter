package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep16(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 7",
        body  = "Si queremos quitar la norma pulsamos en la \"x\" situada a la derecha de dicha norma.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec16.jpg"
        )
    )
