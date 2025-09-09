package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun SettingsTutorialStep1(): TutorialStep = TutorialStep(
    title = R.string.settings_tutorial_title,
    body  = R.string.settings_tutorial_step1,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/SettingsTutorial/Spec1.jpg"
    )
)
