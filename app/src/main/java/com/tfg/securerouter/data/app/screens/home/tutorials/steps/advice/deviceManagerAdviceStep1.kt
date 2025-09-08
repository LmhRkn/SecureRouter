package com.tfg.securerouter.data.app.screens.home.tutorials.steps.advice

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun HomeAdviceStep1(): TutorialStep = TutorialStep(
        title = "IMPORTANTE",
        body  = "Es buena practica revisar cada día si hay dispositivos nuevos.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/Global/Alert.png"
        )
    )
