package com.tfg.securerouter.ui.app.screens.wifi.components.speedtest

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfg.securerouter.data.app.screens.wifi.model.SpeedData
import com.tfg.securerouter.data.app.screens.wifi.model.SpeedtestUiState
import com.tfg.securerouter.data.router.shUsingLaunch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

// -------------------- PUBLIC COMPOSABLE --------------------

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WifiSpeedtest(
    modifier: Modifier = Modifier,
    preferJson: Boolean = true
) {
    var uiState by remember { mutableStateOf<SpeedtestUiState>(SpeedtestUiState.Idle) }
    val scope = rememberCoroutineScope()

    val runTest: suspend () -> Unit = remember(preferJson) {
        {
            uiState = SpeedtestUiState.Running
            val result = runSpeedtestShell(preferJson = preferJson)
            uiState = if (result.isSuccess) {
                SpeedtestUiState.Result(result.getOrThrow())
            } else {
                SpeedtestUiState.Error(result.exceptionOrNull()?.message ?: "Fallo al ejecutar speedtest")
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 1.dp
    ) {
        when (val s = uiState) {
            is SpeedtestUiState.Running -> LoadingCard(onCancel = null)

            is SpeedtestUiState.Result -> ResultCard(
                data = s.data,
                onRunAgain = { scope.launch { runTest() } }
            )

            is SpeedtestUiState.Error -> ErrorCard(
                message = s.message,
                onRetry = { scope.launch { runTest() } }
            )

            SpeedtestUiState.Idle -> {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Iniciar test", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Mide la velocidad de tu conexión.\n" +
                                "Aceptaremos licencia y privacidad si es la primera vez.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(18.dp))
                    Button(
                        onClick = { scope.launch { runTest() } },
                        shape = RoundedCornerShape(16.dp)
                    ) { Text("Empezar") }
                }
            }
        }
    }
}

// -------------------- CARDS & WIDGETS --------------------

@Composable
private fun LoadingCard(onCancel: (() -> Unit)?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Midiendo…", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(14.dp))
        SweepLoader(size = 136.dp, stroke = 12.dp)   // un poco más pequeño
        Spacer(Modifier.height(12.dp))
        Text(
            "Esto puede tardar unos segundos.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (onCancel != null) {
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onCancel) { Text("Cancelar") }
        }
    }
}

/** Anillo animado moderno (sweep + pulso). */
@Composable
private fun SweepLoader(size: Dp, stroke: Dp) {
    val transition = rememberInfiniteTransition(label = "sweep")
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        ),
        label = "sweepAngle"
    )
    val pulse by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 900, easing = FastOutSlowInEasing)
        ),
        label = "pulse"
    )

    val px = with(LocalDensity.current) { size.toPx() }
    val st = with(LocalDensity.current) { stroke.toPx() }
    val brush = Brush.sweepGradient(
        listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        )
    )

    Canvas(modifier = Modifier.size(size)) {
        val dim = px * pulse
        val topLeft = Offset(center.x - dim / 2, center.y - dim / 2)
        drawArc(
            brush = brush,
            startAngle = sweep,
            sweepAngle = 270f,
            useCenter = false,
            topLeft = topLeft,
            size = Size(dim, dim),
            style = Stroke(width = st, cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun ResultCard(data: SpeedData, onRunAgain: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(20.dp)) {
        Text("Resultado", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Download",
                value = data.downloadMbps?.let { formatMbps(it) } ?: "—"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Upload",
                value = data.uploadMbps?.let { formatMbps(it) } ?: "—"
            )
        }

        Spacer(Modifier.height(12.dp))

        FlowRow(Modifier.fillMaxWidth(), horizontalGap = 8.dp, verticalGap = 8.dp) {
            InfoChip("Latencia", data.idleLatencyMs?.let { "${fmt(it)} ms" } ?: "—")
            InfoChip("Jitter",   data.jitterMs?.let      { "${fmt(it)} ms" } ?: "—")
            InfoChip("Pérdida",  data.packetLossPct?.let { "${fmt(it)} %" }  ?: "—")
        }

        Spacer(Modifier.height(8.dp))

        data.server?.let {
            Text("Servidor: $it", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        data.isp?.let {
            Text("ISP: $it", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        // NO mostramos resultUrl

        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onRunAgain, shape = RoundedCornerShape(12.dp)) {
            Text("Repetir prueba")
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No se pudo ejecutar la prueba", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry, shape = RoundedCornerShape(12.dp)) { Text("Reintentar") }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, title: String, value: String) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 3.dp
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(value, style = MaterialTheme.typography.displaySmall.copy(fontSize = 36.sp))
                Spacer(Modifier.width(6.dp))
                Text("Mbps", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Surface(shape = RoundedCornerShape(50), tonalElevation = 2.dp) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("$label: ", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.labelLarge)
        }
    }
}

// -------------------- SHELL + PARSERS --------------------

/** Ejecuta speedtest aceptando licencia/GDPR y devuelve datos parseados. */
private suspend fun runSpeedtestShell(preferJson: Boolean): Result<SpeedData> = withContext(Dispatchers.IO) {
    try {
        if (preferJson) {
            val json = shUsingLaunch(
                "(speedtest --accept-license --accept-gdpr -f json 2>/dev/null || true) | tail -n +1"
            ).trim()
            if (json.startsWith("{") && json.contains("download") && json.contains("upload")) {
                return@withContext Result.success(parseJson(json))
            }
        }
        val txt = shUsingLaunch("(speedtest --accept-license --accept-gdpr 2>/dev/null || true)")
        if (txt.isBlank()) error("Salida vacía de speedtest")
        Result.success(parsePlainText(txt))
    } catch (t: Throwable) {
        Result.failure(t)
    }
}

/** Parser muy ligero para el JSON de Ookla CLI (sin dependencias). */
private fun parseJson(json: String): SpeedData {
    fun num(key: String): Double? =
        Regex(""""$key"\s*:\s*([-+]?\d+(\.\d+)?)""").find(json)?.groupValues?.get(1)?.toDoubleOrNull()

    val dlBandwidth = Regex(""""download"\s*:\s*\{[^}]*"bandwidth"\s*:\s*(\d+)""").find(json)?.groupValues?.get(1)?.toDoubleOrNull()
    val ulBandwidth = Regex(""""upload"\s*:\s*\{[^}]*"bandwidth"\s*:\s*(\d+)""").find(json)?.groupValues?.get(1)?.toDoubleOrNull()

    val downloadMbps = dlBandwidth?.let { (it * 8.0) / 1_000_000.0 }
    val uploadMbps   = ulBandwidth?.let { (it * 8.0) / 1_000_000.0 }

    val idleLatency = num("latency")
    val jitter = num("jitter")
    val packet = num("packetLoss")

    val isp = Regex(""""isp"\s*:\s*"([^"]+)"""").find(json)?.groupValues?.get(1)
    val serverName = Regex(""""server"\s*:\s*\{[^}]*"name"\s*:\s*"([^"]+)"""").find(json)?.groupValues?.get(1)
    val serverLoc = Regex(""""server"\s*:\s*\{[^}]*"location"\s*:\s*"([^"]+)"""").find(json)?.groupValues?.get(1)
    val server = listOfNotNull(serverName, serverLoc).joinToString(" — ").ifBlank { null }

    val url = Regex(""""result"\s*:\s*\{[^}]*"url"\s*:\s*"([^"]+)"""").find(json)?.groupValues?.get(1)

    return SpeedData(
        downloadMbps = downloadMbps,
        uploadMbps = uploadMbps,
        idleLatencyMs = idleLatency,
        jitterMs = jitter,
        packetLossPct = packet,
        server = server,
        isp = isp,
        resultUrl = url
    )
}

/** Parser del texto plano (como el que imprime el CLI). */
private fun parsePlainText(txt: String): SpeedData {
    fun r(pattern: String) = Regex(pattern, RegexOption.MULTILINE)
    val server = r("""^\s*Server:\s*(.+?)\s*-\s*(.+?)\s*\(id:\s*\d+\)""").find(txt)?.let {
        "${it.groupValues[1].trim()} — ${it.groupValues[2].trim()}"
    }
    val isp = r("""^\s*ISP:\s*(.+)$""").find(txt)?.groupValues?.get(1)?.trim()
    val idle = r("""Idle Latency:\s*([\d.]+)\s*ms""").find(txt)?.groupValues?.get(1)?.toDoubleOrNull()
    val jitter = r("""jitter:\s*([\d.]+)ms""").find(txt)?.groupValues?.get(1)?.toDoubleOrNull()
    val dMbps = r("""Download:\s*([\d.]+)\s*Mbps""").find(txt)?.groupValues?.get(1)?.toDoubleOrNull()
    val uMbps = r("""Upload:\s*([\d.]+)\s*Mbps""").find(txt)?.groupValues?.get(1)?.toDoubleOrNull()
    val loss = r("""Packet Loss:\s*([\d.]+)%""").find(txt)?.groupValues?.get(1)?.toDoubleOrNull()
    val url = r("""Result URL:\s*(\S+)""").find(txt)?.groupValues?.get(1)

    return SpeedData(
        downloadMbps = dMbps,
        uploadMbps = uMbps,
        idleLatencyMs = idle,
        jitterMs = jitter,
        packetLossPct = loss,
        server = server,
        isp = isp,
        resultUrl = url
    )
}

// -------------------- HELPERS --------------------

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalGap: Dp = 8.dp,
    verticalGap: Dp = 8.dp,
    content: @Composable FlowRowScope.() -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(horizontalGap),
        verticalArrangement = Arrangement.spacedBy(verticalGap),
        content = content
    )
}

private fun fmt(v: Double, digits: Int = 2): String =
    "%.${digits}f".format(Locale.US, v)

private fun formatMbps(v: Double): String =
    when {
        v >= 1000 -> fmt(v, 0)
        v >= 100  -> fmt(v, 1)
        else      -> fmt(v, 2)
    }
