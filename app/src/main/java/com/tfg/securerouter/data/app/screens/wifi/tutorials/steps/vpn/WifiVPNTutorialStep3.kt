package com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialStep

fun WifiVPNTutorialStep3(): TutorialStep = TutorialStep(
    title = R.string.wifi_vpn_tutorial_my_public_ip_title,
    body  = R.string.wifi_vpn_tutorial_my_public_ip_step_3,
    media = NoticeMedia.Url(
        "https://raw.githubusercontent.com/ElMarkoos/SecureRouterAssets/main/WifiTutorial/VPN/Spec3.jpg"
    )
)
