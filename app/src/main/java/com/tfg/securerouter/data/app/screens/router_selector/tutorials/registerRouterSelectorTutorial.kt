package com.tfg.securerouter.data.app.screens.home.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep1
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep2
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep3
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep4
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep5
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep6
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep7
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.RouterSelectorTutorialStep8
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.SettingsTutorialStep1


@Composable
fun RegisterRotuerSelectorTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                RouterSelectorTutorialStep1(),
                RouterSelectorTutorialStep2(),
                RouterSelectorTutorialStep3(),
                RouterSelectorTutorialStep4(),
                RouterSelectorTutorialStep5(),
                RouterSelectorTutorialStep6(),
                RouterSelectorTutorialStep7(),
                RouterSelectorTutorialStep8(),
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}