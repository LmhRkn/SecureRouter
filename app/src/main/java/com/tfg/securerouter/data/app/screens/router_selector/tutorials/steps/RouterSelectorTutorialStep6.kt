package com.tfg.securerouter.data.app.screens.settings.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun RouterSelectorTutorialStep6(): TutorialStep = TutorialStep(
    title = R.string.router_selector_tutorial_enter_router_title,
    body  = R.string.router_selector_tutorial_step6,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/Global/Alert.png"
    )
)
