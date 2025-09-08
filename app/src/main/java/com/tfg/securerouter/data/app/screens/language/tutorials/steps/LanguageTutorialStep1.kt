package com.tfg.securerouter.data.app.screens.home.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun LanguageTutorialStep1(): TutorialStep = TutorialStep(
        title = "Idioma",
        body  = "Aquí puede cambiar el idioma que se mostrará en la aplicación.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/LanguageTutorial/Spec1.jpg"
        )
    )
