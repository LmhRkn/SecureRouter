package com.tfg.securerouter.data.app.screens.wifi.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep1
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep10
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep11
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep12
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep13
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep14
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep2
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep3
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep4
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep5
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep6
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep7
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep8
import com.tfg.securerouter.data.app.screens.wifi.tutorials.steps.ad_block.WifiTutorialStep9
import com.tfg.securerouter.data.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.ui.notice.tutorials.TutorialCenter


@Composable
fun WifiTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                WifiTutorialStep1(),
                WifiTutorialStep2(),
                WifiTutorialStep3(),
                WifiTutorialStep4(),
                WifiTutorialStep5(),
                WifiTutorialStep6(),
                WifiTutorialStep7(),
                WifiTutorialStep8(),
                WifiTutorialStep9(),
                WifiTutorialStep10(),
                WifiTutorialStep11(),
                WifiTutorialStep12(),
                WifiTutorialStep13(),
                WifiTutorialStep14(),
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}