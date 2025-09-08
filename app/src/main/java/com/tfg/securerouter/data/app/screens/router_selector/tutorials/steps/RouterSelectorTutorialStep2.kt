package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep2(): TutorialStep = TutorialStep(
        title = "Pantalla Selección del Ruter",
        body  = "Cuando detecta un router nuevo, te saldrá el icono que indica que es un nuevo router.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/RouterSelectorTutorial/Spec2.jpg"
        )
    )
