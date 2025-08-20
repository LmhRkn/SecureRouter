package com.tfg.securerouter.data.app.screens.home.tutorials.steps

import com.tfg.securerouter.data.notice.model.NoticeMedia
import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

fun HomeTutorialStep1(): TutorialStep = TutorialStep(
        title = "¡Bienvenido a SecureRouter!",
        body  = "Configura tu red y revisa los dispositivos conectados.",
        media = NoticeMedia.Url(
            "https://images.unsplash.com/photo-1520975916090-3105956dac38?q=80&w=800&auto=format&fit=crop"
        )
    )
