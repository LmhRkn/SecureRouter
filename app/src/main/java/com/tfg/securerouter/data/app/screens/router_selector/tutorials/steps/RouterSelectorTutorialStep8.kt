package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep8(): TutorialStep = TutorialStep(
        title = "Entrar a un router pt. 4",
        body  = "Si ya alguien se ha conectado antes te pedirá la contraseña que ya existe, te la tiene que aportar el usuario que la haya conectado.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/RouterSelectorTutorial/Spec8.jpg"
        )
    )
