package com.tfg.securerouter.data.app.screens.device_manager.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.data.app.screens.device_manager.tutorials.steps.DeviceMAnagerTutorialStep1
import com.tfg.securerouter.data.app.screens.device_manager.tutorials.steps.advice.DeviceManagerAdviceStep1
import com.tfg.securerouter.data.app.screens.device_manager.tutorials.steps.advice.DeviceManagerAdviceStep2
import com.tfg.securerouter.data.app.screens.home.tutorials.steps.advice.DeviceManagerAdviceStep3
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.DeviceMAnagerTutorialStep2
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.DeviceMAnagerTutorialStep3
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.DeviceMAnagerTutorialStep4
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.DeviceMAnagerTutorialStep5
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.DeviceMAnagerTutorialStep6
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter


@Composable
fun RegisterDeviceManagerTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                DeviceManagerAdviceStep1(),
                DeviceManagerAdviceStep2(),
                DeviceManagerAdviceStep3(),
                DeviceMAnagerTutorialStep1(),
                DeviceMAnagerTutorialStep2(),
                DeviceMAnagerTutorialStep3(),
                DeviceMAnagerTutorialStep4(),
                DeviceMAnagerTutorialStep5(),
                DeviceMAnagerTutorialStep6(),
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}