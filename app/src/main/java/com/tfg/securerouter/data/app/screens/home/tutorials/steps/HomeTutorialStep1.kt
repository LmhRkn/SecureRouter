package com.tfg.securerouter.data.app.screens.home.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun HomeTutorialStep1(): TutorialStep = TutorialStep(
    title = R.string.home_tutorial_step1_title,
    body  = R.string.home_tutorial_step1_body,
    media = NoticeMedia.Resource(R.mipmap.ic_launcher)
)
