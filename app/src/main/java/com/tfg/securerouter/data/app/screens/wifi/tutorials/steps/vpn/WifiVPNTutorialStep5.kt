package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep5(): TutorialStep = TutorialStep(
    title = R.string.wifi_vpn_tutorial_noip_title,
    body  = R.string.wifi_vpn_tutorial_noip_step_5,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec5.jpg"
    )
)
