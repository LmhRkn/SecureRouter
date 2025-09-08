package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep1(): TutorialStep = TutorialStep(
        title = "Pantalla Filtros",
        body  = "Aquí se bloquea a todos los dispositivos a la vez",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec1.jpg"
        )
    )
