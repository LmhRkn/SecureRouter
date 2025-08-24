    package com.tfg.securerouter.ui.notice.tutorials

    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.ArrowForward
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
    import coil.decode.GifDecoder.*
    import coil.request.ImageRequest
    import coil.request.ImageRequest.*
    import com.tfg.securerouter.data.notice.model.NoticeMedia
    import com.tfg.securerouter.data.notice.model.tutorials.TutorialSpec
    import com.tfg.securerouter.data.notice.model.tutorials.TutorialStep

    @Composable
    fun TutorialModal(
        spec: TutorialSpec,
        onSkip: () -> Unit,
        onFinish: () -> Unit
    ) {
        var currentIndex by remember { mutableStateOf(spec.startIndex.coerceIn(0, spec.steps.lastIndex)) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TutorialStepContent(step = spec.steps[currentIndex])

                    Spacer(Modifier.weight(1f))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { if (currentIndex > 0) currentIndex-- },
                            enabled = currentIndex > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atrás"
                            )
                        }

                        if (currentIndex < spec.steps.lastIndex) {
                            IconButton(
                                onClick = { currentIndex++ }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Siguiente"
                                )
                            }
                        } else {
                            Button(onClick = onFinish) {
                                Text("Finalizar")
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    if (spec.skippable) {
                        OutlinedButton(onClick = onSkip) {
                            Text("Saltar")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun TutorialStepContent(step: TutorialStep) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                step.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))

            when (val media = step.media) {
                is NoticeMedia.Url -> {
                    val request = Builder(LocalContext.current)
                        .data(media.url)
                        .decoderFactory(Factory())
                        .crossfade(true)
                        .build()
                    AsyncImage(
                        model = request,
                        contentDescription = step.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(Modifier.height(12.dp))
                }

                is NoticeMedia.Resource -> {
                    val request = Builder(LocalContext.current)
                        .data(media.resId)
                        .decoderFactory(Factory())
                        .crossfade(true)
                        .build()
                    AsyncImage(
                        model = request,
                        contentDescription = step.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(Modifier.height(12.dp))
                }

                NoticeMedia.None -> {
                }

                is NoticeMedia.AsciiMonospace -> TODO()
                is NoticeMedia.Base64Image -> TODO()
            }

            step.body?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
