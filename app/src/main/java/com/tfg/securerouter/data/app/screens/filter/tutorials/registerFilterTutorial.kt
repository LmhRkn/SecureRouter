package com.tfg.securerouter.data.app.screens.filter.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep1
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep10
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep11
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep12
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep13
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep14
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep15
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep16
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep2
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep3
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep4
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep5
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep6
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep7
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep8
import com.tfg.securerouter.data.app.screens.filter.tutorials.steps.FilterTutorialStep9
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.data.app.screens.settings.tutorials.steps.SettingsTutorialStep1


@Composable
fun RegisterFilterTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                FilterTutorialStep1(),
                FilterTutorialStep2(),
                FilterTutorialStep3(),
                FilterTutorialStep4(),
                FilterTutorialStep5(),
                FilterTutorialStep6(),
                FilterTutorialStep7(),
                FilterTutorialStep8(),
                FilterTutorialStep9(),
                FilterTutorialStep10(),
                FilterTutorialStep11(),
                FilterTutorialStep12(),
                FilterTutorialStep13(),
                FilterTutorialStep14(),
                FilterTutorialStep15(),
                FilterTutorialStep16(),
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}