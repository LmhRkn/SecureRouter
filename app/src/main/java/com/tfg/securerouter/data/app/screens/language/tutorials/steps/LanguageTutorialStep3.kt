package com.tfg.securerouter.data.app.screens.home.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun LanguageTutorialStep3(): TutorialStep = TutorialStep(
        title = "Cambiar Idioma pt. 2",
        body  = "Y pulsas en aplicar.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/LanguageTutorial/Spec3.jpg"
        )
    )
