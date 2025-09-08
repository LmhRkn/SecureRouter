package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep1(): TutorialStep = TutorialStep(
        title = "Pantalla Selección del Ruter",
        body  = "Aquí te aparecen todos los routers que tienes guardados.\n"+
                "Además, sale el estado de cada router y si puedes acceder a ellos.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/RouterSelectorTutorial/Spec1.jpg"
        )
    )
