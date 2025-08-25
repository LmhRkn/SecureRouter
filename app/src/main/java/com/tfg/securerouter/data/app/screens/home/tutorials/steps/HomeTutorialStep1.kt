package com.tfg.securerouter.data.app.screens.home.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun HomeTutorialStep1(): TutorialStep = TutorialStep(
        title = "¡Bienvenido a SecureRouter!",
        body  = "Configura tu red y revisa los dispositivos conectados.",
        media = NoticeMedia.Resource(R.mipmap.ic_launcher)
    )
