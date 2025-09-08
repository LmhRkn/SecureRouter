package com.tfg.securerouter.data.app.screens.devices_options.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep1
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep2
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep3
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep4
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep5
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep6
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep7
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep8
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep9
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep10
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep11
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep12
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep13
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep14
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.DevicesOptionTutorialStep15
import com.tfg.securerouter.data.app.screens.devices_options.tutorials.steps.advice.DeviceOptionsAdviceStep1

@Composable
fun RegisterDevicesOptionTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                DeviceOptionsAdviceStep1(),
                DevicesOptionTutorialStep1(),
                DevicesOptionTutorialStep2(),
                DevicesOptionTutorialStep3(),
                DevicesOptionTutorialStep4(),
                DevicesOptionTutorialStep5(),
                DevicesOptionTutorialStep6(),
                DevicesOptionTutorialStep7(),
                DevicesOptionTutorialStep8(),
                DevicesOptionTutorialStep9(),
                DevicesOptionTutorialStep10(),
                DevicesOptionTutorialStep11(),
                DevicesOptionTutorialStep12(),
                DevicesOptionTutorialStep13(),
                DevicesOptionTutorialStep14(),
                DevicesOptionTutorialStep15(),
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}