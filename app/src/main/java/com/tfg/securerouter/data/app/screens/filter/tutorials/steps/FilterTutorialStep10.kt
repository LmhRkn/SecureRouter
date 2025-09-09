package com.tfg.securerouter.data.app.screens.filter.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun FilterTutorialStep10(): TutorialStep = TutorialStep(
    title = R.string.filter_tutorial_block_webs_title,
    body  = R.string.filter_tutorial_block_webs_spec_10,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/FilterTutorial/Spec10.jpg"
    )
)

