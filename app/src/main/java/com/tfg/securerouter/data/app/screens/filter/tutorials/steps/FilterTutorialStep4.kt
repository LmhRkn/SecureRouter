package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep4(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 3",
        body  = "Si queremos limitar el acceso a todos los dispositivos en una franja horaria contraria y unos dias concretos de la semana, pulsamos en el botón \"Añadir regla\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec4.jpg"
        )
    )
