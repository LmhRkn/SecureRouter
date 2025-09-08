package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep5(): TutorialStep = TutorialStep(
        title = "Entrar a un router pt. 1",
        body  = "Si nunca te has conectado a un router y además es el primer usuario en conectarse al router con la aplicación. Te pedirá que configures la contraseña de acceso.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/RouterSelectorTutorial/Spec5.jpg"
        )
    )
