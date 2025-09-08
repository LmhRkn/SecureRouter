package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep6(): TutorialStep = TutorialStep(
        title = "Entrar a un router pt. 2",
        body  = "Aviso: Esta contraseña es la que se usa para entrar con la aplicación, no es la misma que se usa para conectarse al router para tener acceso a internet.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/Global/Alert.png"
        )
    )
