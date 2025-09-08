package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep5(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 4",
        body  = "Seleccionamos la hora a la que se deja de poder conectar y a la hora a la se vuelve a poder conectar.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec5.jpg"
        )
    )
