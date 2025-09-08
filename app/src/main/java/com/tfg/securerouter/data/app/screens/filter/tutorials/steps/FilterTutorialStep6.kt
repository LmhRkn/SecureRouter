package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep6(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 5",
        body  = "Seleccionamos los dias de la semana en los que queremos que se haag efecto la norma.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec6.jpg"
        )
    )
