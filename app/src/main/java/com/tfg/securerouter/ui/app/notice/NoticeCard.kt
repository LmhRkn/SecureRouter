package com.tfg.securerouter.ui.app.notice

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.tfg.securerouter.data.app.notice.model.NoticeActionRole
import com.tfg.securerouter.data.app.notice.model.NoticeDefaults
import com.tfg.securerouter.data.app.notice.model.NoticeMedia
import com.tfg.securerouter.data.app.notice.model.NoticeSpec
import kotlinx.coroutines.delay
import com.tfg.securerouter.R

@Composable
fun NoticeCard(
    spec: NoticeSpec,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val colors = NoticeDefaults.colorsFor(spec.type)
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.container,
        contentColor = colors.content,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                if (spec.media !is NoticeMedia.None) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) { NoticeMediaView(spec.media) }
                    Spacer(Modifier.width(12.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text(
                        spec.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    spec.body?.let {
                        Spacer(Modifier.height(6.dp))
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (spec.dismissible && onDismiss != null) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "✕",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onDismiss() },
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            if (spec.actions.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    spec.actions.forEach { a ->
                        when (a.role) {
                            NoticeActionRole.Primary ->
                                Button(onClick = { a.onClick?.invoke() }) { Text(a.label) }
                            NoticeActionRole.Secondary ->
                                OutlinedButton(onClick = { a.onClick?.invoke() }) { Text(a.label) }
                            NoticeActionRole.Destructive ->
                                Button(
                                    onClick = { a.onClick?.invoke() },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) { Text(a.label, color = MaterialTheme.colorScheme.onError) }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(spec.autoDismissMillis) {
        val ms = spec.autoDismissMillis
        if (ms != null && ms > 0) {
            delay(ms)
            onDismiss?.invoke()
        }
    }
}

@Composable
private fun NoticeMediaView(media: NoticeMedia) {
    val ctx = LocalContext.current
    when (media) {
        is NoticeMedia.Url -> {
            val req = ImageRequest.Builder(ctx)
                .data(media.url)
                .decoderFactory(GifDecoder.Factory())
                .crossfade(true)
                .build()
            AsyncImage(
                model = req,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        is NoticeMedia.Resource -> {
            AsyncImage(
                model = media.resId,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        is NoticeMedia.Base64Image -> {
            val bitmap = remember(media.base64) {
                runCatching {
                    val bytes = Base64.decode(media.base64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }.getOrNull()
            }
            if (bitmap != null) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp, max = 360.dp)
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = stringResource(R.string.qr_content_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.image_base64_invalid_error),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is NoticeMedia.AsciiMonospace -> {
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 360.dp)
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
            ) {
                Text(
                    media.text,
                    fontFamily = FontFamily.Monospace,
                    softWrap = false,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        NoticeMedia.None -> Unit
    }
}
