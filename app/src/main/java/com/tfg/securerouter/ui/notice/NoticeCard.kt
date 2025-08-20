package com.tfg.securerouter.ui.notice

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest

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
                    Text(spec.title, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    spec.body?.let {
                        Spacer(Modifier.height(6.dp))
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (spec.dismissible && onDismiss != null) {
                    Spacer(Modifier.width(8.dp))
                    Text("✕",
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
        Log.d("NoticeCard", "LaunchedEffect: autoDismissMillis=${spec.autoDismissMillis}")
        val ms = spec.autoDismissMillis
        if (ms != null && ms > 0) {
            kotlinx.coroutines.delay(ms)
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
            AsyncImage(model = req, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        }
        is NoticeMedia.Resource ->
            AsyncImage(model = media.resId, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        NoticeMedia.None -> Unit
    }
}
