package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep4(): TutorialStep = TutorialStep(
    title = R.string.filter_tutorial_block_time_title,
    body  = R.string.filter_tutorial_block_time_spec_4,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec4.jpg"
    )
)
