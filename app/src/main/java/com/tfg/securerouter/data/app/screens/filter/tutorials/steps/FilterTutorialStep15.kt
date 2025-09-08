package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep15(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 6",
        body  = "Con esto hemos agregado la regla",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec15.jpg"
        )
    )
