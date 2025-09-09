package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep2(): TutorialStep = TutorialStep(
    title = R.string.router_selector_tutorial_title,
    body  = R.string.router_selector_tutorial_step2,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/RouterSelectorTutorial/Spec2.jpg"
    )
)
