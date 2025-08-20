package com.tfg.securerouter.data.notice.model.tutorials

import androidx.compose.runtime.Immutable
import com.tfg.securerouter.ui.notice.NoticeMedia

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
