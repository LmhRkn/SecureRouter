package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep10(): TutorialStep = TutorialStep(
        title = "Bloqueo acceso a webs pt. 1",
        body  = "Para poder bloquear el acceso a una web, pulsamos en el símbolo \"+\" de el apartado \"filtros\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec10.jpg"
        )
    )
