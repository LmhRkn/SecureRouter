package com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun DevicesOptionTutorialStep11(): TutorialStep = TutorialStep(
    title = R.string.device_options_tutorial_block_time_title,
    body = R.string.device_options_tutorial_block_time_spec_11,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
    )
)
