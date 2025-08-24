package com.tfg.securerouter.data.notice.model

import androidx.annotation.DrawableRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
sealed class NoticeMedia {
    data class Url(val url: String) : NoticeMedia()
    data class Resource(@DrawableRes val resId: Int) : NoticeMedia()
    object None : NoticeMedia()

    data class Base64Image(val base64: String) : NoticeMedia()
    data class AsciiMonospace(val text: String) : NoticeMedia()
}

enum class NoticeType { Info, Success, Warning, Error }
enum class NoticeActionRole { Primary, Secondary, Destructive }

@Immutable
data class NoticeAction(
    val label: String,
    val role: NoticeActionRole = NoticeActionRole.Primary,
    val onClick: (() -> Unit)? = null
)

@Immutable
data class NoticeSpec(
    val title: String,
    val body: String? = null,
    val media: NoticeMedia = NoticeMedia.None,
    val type: NoticeType = NoticeType.Info,
    val actions: List<NoticeAction> = emptyList(),
    val dismissible: Boolean = false,
    val autoDismissMillis: Long? = null,
)

object NoticeDefaults {
    data class Colors(val container: Color, val content: Color, val border: Color)

    @Composable
    fun colorsFor(type: NoticeType) = when (type) {
        NoticeType.Info -> Colors(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            MaterialTheme.colorScheme.outline
        )
        NoticeType.Success -> Colors(Color(0xFFE8F5E9), Color(0xFF1B5E20), Color(0xFF66BB6A))
        NoticeType.Warning -> Colors(Color(0xFFFFF8E1), Color(0xFF7B5E00), Color(0xFFFFCA28))
        NoticeType.Error -> Colors(Color(0xFFFFEBEE), Color(0xFFB71C1C), Color(0xFFE57373))
    }

    fun info(title: String, body: String? = null, media: NoticeMedia = NoticeMedia.None,
             actions: List<NoticeAction> = emptyList(), autoDismissMillis: Long? = null) =
        NoticeSpec(title, body, media, NoticeType.Info, actions, true, autoDismissMillis)

    fun success(title: String, body: String? = null, media: NoticeMedia = NoticeMedia.None,
                actions: List<NoticeAction> = emptyList(), autoDismissMillis: Long? = null) =
        NoticeSpec(title, body, media, NoticeType.Success, actions, true, autoDismissMillis)

    fun warning(title: String, body: String? = null, media: NoticeMedia = NoticeMedia.None,
                actions: List<NoticeAction> = emptyList(), autoDismissMillis: Long? = null) =
        NoticeSpec(title, body, media, NoticeType.Warning, actions, true, autoDismissMillis)

    fun error(title: String, body: String? = null, media: NoticeMedia = NoticeMedia.None,
              actions: List<NoticeAction> = emptyList(), autoDismissMillis: Long? = null) =
        NoticeSpec(title, body, media, NoticeType.Error, actions, true, autoDismissMillis)
}
