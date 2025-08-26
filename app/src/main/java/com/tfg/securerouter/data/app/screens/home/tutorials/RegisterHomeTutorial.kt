package com.tfg.securerouter.data.app.screens.home.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.data.app.screens.home.tutorials.steps.HomeTutorialStep1
import com.tfg.securerouter.data.app.screens.home.tutorials.steps.HomeTutorialStep2


@Composable
fun RegisterHomeTutorial() {
    val tutorial = remember {
        TutorialSpec(
            steps = listOf(
                HomeTutorialStep1(),
                HomeTutorialStep2()
            ),
            startIndex = 0,
            skippable = true
        )
    }

    LaunchedEffect(Unit) {
        TutorialCenter.register(tutorial)
    }
}