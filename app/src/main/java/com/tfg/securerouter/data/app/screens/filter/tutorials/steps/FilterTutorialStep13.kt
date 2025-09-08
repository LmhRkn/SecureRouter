package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep13(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 4",
        body  = "Escribimos el dominio que queremos bloquer (Ejemplo: elpais.com).",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec13.jpg"
        )
    )
