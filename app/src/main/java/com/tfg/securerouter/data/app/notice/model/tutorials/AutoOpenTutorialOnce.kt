// com.tfg.securerouter.ui.notice.tutorials.AutoOpenTutorialOnce.kt
package com.tfg.securerouter.ui.notice.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.tfg.securerouter.data.app.notice.model.tutorials.TutorialOnce
import com.tfg.securerouter.ui.app.notice.tutorials.TutorialCenter
import com.tfg.securerouter.ui.app.notice.tutorials.UiReadyCenter

@Composable
fun AutoOpenTutorialOnce(routerId: Int?, screenKey: String) {
    val uiReady = UiReadyCenter.ready.collectAsState().value

    LaunchedEffect(routerId, uiReady) {
        if (uiReady && TutorialCenter.hasTutorial() &&
            TutorialOnce.shouldAutoOpen(screenKey)
        ) {
            TutorialCenter.open()
            TutorialOnce.markShown(screenKey)
        }
    }
}
