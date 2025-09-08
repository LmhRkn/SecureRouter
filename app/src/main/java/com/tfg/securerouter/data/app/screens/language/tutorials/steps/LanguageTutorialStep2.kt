package com.tfg.securerouter.data.app.screens.home.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun LanguageTutorialStep2(): TutorialStep = TutorialStep(
        title = "Cambiar Idioma pt. 1",
        body  = "Selecciona el idioma que quieras que tenga la aplicación.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/LanguageTutorial/Spec2.jpg"
        )
    )
