package com.tfg.securerouter.data.app.screens.wifi.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep1
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep10
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep11
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep12
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep13
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep14
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep2
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep3
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep4
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep5
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep6
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep7
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep8
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiAdBlockerTutorialStep9
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep1
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep10
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep11
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep12
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep13
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep14
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep15
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep16
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep17
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep18
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep19
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep2
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep20
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep21
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep22
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep23
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep24
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep25
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep3
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep4
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep5
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep6
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep7
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep8
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.vpn.WifiVPNTutorialStep9
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter


@Composable
fun WifiTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                WifiAdBlockerTutorialStep1(),
                WifiAdBlockerTutorialStep2(),
                WifiAdBlockerTutorialStep3(),
                WifiAdBlockerTutorialStep4(),
                WifiAdBlockerTutorialStep5(),
                WifiAdBlockerTutorialStep6(),
                WifiAdBlockerTutorialStep7(),
                WifiAdBlockerTutorialStep8(),
                WifiAdBlockerTutorialStep9(),
                WifiAdBlockerTutorialStep10(),
                WifiAdBlockerTutorialStep11(),
                WifiAdBlockerTutorialStep12(),
                WifiAdBlockerTutorialStep13(),
                WifiAdBlockerTutorialStep14(),
                WifiVPNTutorialStep1(),
                WifiVPNTutorialStep2(),
                WifiVPNTutorialStep3(),
                WifiVPNTutorialStep4(),
                WifiVPNTutorialStep5(),
                WifiVPNTutorialStep6(),
                WifiVPNTutorialStep7(),
                WifiVPNTutorialStep8(),
                WifiVPNTutorialStep9(),
                WifiVPNTutorialStep10(),
                WifiVPNTutorialStep11(),
                WifiVPNTutorialStep12(),
                WifiVPNTutorialStep13(),
                WifiVPNTutorialStep14(),
                WifiVPNTutorialStep15(),
                WifiVPNTutorialStep16(),
                WifiVPNTutorialStep17(),
                WifiVPNTutorialStep18(),
                WifiVPNTutorialStep19(),
                WifiVPNTutorialStep20(),
                WifiVPNTutorialStep21(),
                WifiVPNTutorialStep22(),
                WifiVPNTutorialStep23(),
                WifiVPNTutorialStep24(),
                WifiVPNTutorialStep25(),
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}