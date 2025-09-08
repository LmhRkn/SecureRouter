package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep4(): TutorialStep = TutorialStep(
        title = "Pantalla Selección del Ruter",
        body  = "Si no sale ningún icono, significa que el router no está conectado y no es accesible.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/RouterSelectorTutorial/Spec4.jpg"
        )
    )
