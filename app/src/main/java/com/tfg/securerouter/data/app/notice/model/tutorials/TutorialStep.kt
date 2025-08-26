package com.tfg.securerouter.data.app.notice.model.tutorials

import androidx.compose.runtime.Immutable
import com.tfg.securerouter.data.app.notice.model.NoticeMedia

@Immutable
data class TutorialStep(
    val title: String,
    val body: String? = null,
    val media: NoticeMedia = NoticeMedia.None
)

@Immutable
data class TutorialSpec(
    val steps: List<TutorialStep>,
    val startIndex: Int = 0,
    val skippable: Boolean = true
)
