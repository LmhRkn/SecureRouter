package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun SettingsTutorialStep1(): TutorialStep = TutorialStep(
        title = "Pantalla Configuración",
        body  = "Puedes cambiar el idioma pulsando en Idioma.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/SettingsTutorial/Spec1.jpg"
        )
    )
