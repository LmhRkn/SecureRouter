package com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DevicesOptionTutorialStep2(): TutorialStep = TutorialStep(
        title = "Cambiar el nombre del dispositivo",
        body  = "Puedes cambiar el nombre del dispositivo dando al botón de editar arriba a la derecha.\n" +
                "Ten en cuenta que el nombre del dispositivo solo estará cambiado para tu aplicación. \n" +
                "Otros usuarios nos verán este cambio.",
        media = NoticeMedia.Url(
            "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
        )
    )
