package com.tfg.securerouter.data.app.screens.home.tutorials.steps.advice

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun HomeAdviceStep2(): TutorialStep = TutorialStep(
    title = R.string.tutorial_important_label,
    body  = R.string.home_save_with_name_hint,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/Global/Alert.png"
    )
)
