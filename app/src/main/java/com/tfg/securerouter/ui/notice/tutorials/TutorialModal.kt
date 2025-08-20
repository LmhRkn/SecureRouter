package com.tfg.securerouter.ui.notice.tutorials

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.tfg.securerouter.data.notice.model.tutorials.TutorialSpec
import com.tfg.securerouter.ui.notice.NoticeMedia

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TutorialModal(
    spec: TutorialSpec,
    onSkip: () -> Unit,
    onFinish: () -> Unit,
    onDismissRequest: (() -> Unit)? = null,
) {
    var idx by remember(spec) { mutableIntStateOf(spec.startIndex.coerceIn(0, spec.steps.lastIndex)) }
    val step = spec.steps[idx]

    val canPrev = idx > 0
    val canNext = idx < spec.steps.lastIndex

    val conf = LocalConfiguration.current
    val targetHeight = (conf.screenHeightDp * 0.75f).dp

    Box(Modifier.fillMaxSize()) {
        val scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f)
        Box(
            Modifier
                .fillMaxSize()
                .consumeClicks { onDismissRequest?.invoke() }
        ) {
            Canvas(Modifier.matchParentSize()) {
                drawRect(scrimColor)
            }
        }

        // tarjeta centrada
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = targetHeight)
                .align(Alignment.Center),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Título
                Text(
                    step.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(12.dp))

                // Contenido con scroll (imagen opcional + texto)
                val scroll = rememberScrollState()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .verticalScroll(scroll)
                ) {
                    when (val m = step.media) {
                        is NoticeMedia.Url -> {
                            val req = ImageRequest.Builder(LocalContext.current)
                                .data(m.url)
                                .decoderFactory(GifDecoder.Factory())
                                .crossfade(true)
                                .build()
                            AsyncImage(
                                model = req,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 160.dp) // da presencia si hay imagen
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                        is NoticeMedia.Resource -> {
                            AsyncImage(
                                model = m.resId,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                        NoticeMedia.None -> Unit // si no hay, el texto ocupa todo
                    }

                    step.body?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Barra inferior: ←  Skip  →
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (canPrev) {
                        IconButton(onClick = { if (canPrev) idx-- }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    } else {
                        Spacer(Modifier.size(48.dp)) // mantiene balanceado el layout
                    }

                    if (spec.skippable) {
                        Button(onClick = onSkip, shape = RoundedCornerShape(50)) {
                            Text("Saltar")
                        }
                    } else {
                        Spacer(Modifier.width(8.dp))
                    }

                    if (canNext) {
                        IconButton(onClick = { if (canNext) idx++ }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Adelante")
                        }
                    } else {
                        // último paso → botón “Finalizar”
                        Button(onClick = onFinish) { Text("Finalizar") }
                    }
                }
            }
        }
    }
}

private fun Modifier.consumeClicks(onClick: () -> Unit) = composed {
    val src = remember { MutableInteractionSource() }
    clickable(interactionSource = src, indication = null) { onClick() }
}
