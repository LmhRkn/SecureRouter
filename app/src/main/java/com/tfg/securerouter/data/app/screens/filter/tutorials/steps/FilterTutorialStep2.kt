package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep2(): TutorialStep = TutorialStep(
        title = "Bloqueo por tiempo pt. 1",
        body  = "Para poder limitar las horas en las que se puede conectar al router, pulsamos en el símbolo \"+\" de el apartado \"tiempos\"",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec2.jpg"
        )
    )
