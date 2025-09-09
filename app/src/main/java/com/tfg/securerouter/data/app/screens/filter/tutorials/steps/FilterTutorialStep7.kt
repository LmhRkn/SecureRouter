package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep7(): TutorialStep = TutorialStep(
    title = R.string.filter_tutorial_block_time_title,
    body  = R.string.filter_tutorial_block_time_spec_7,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec7.jpg"
    )
)

