package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep14(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 5",
        body  = "Y añadimos la regla con el botón \"Añadir\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec14.jpg"
        )
    )
