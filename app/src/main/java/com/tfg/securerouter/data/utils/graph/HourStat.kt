@file:Suppress("unused")

package com.tfg.securerouter.data.utils.graph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.*

/* ---------- MODELOS ---------- */
data class HourStat(val hour: Int, val rxMiB: Double, val txMiB: Double)
data class DayStat(val date: LocalDate, val rxMiB: Double, val txMiB: Double)

/* ---------- PARSER VNSTAT ---------- */
object VnstatParser {
    // "00   0.0   0.0" (se repite en columnas)
    val hourlyTriple =
        Regex("""\b(\d{2})\s+([0-9]+(?:\.[0-9]+)?)\s+([0-9]+(?:\.[0-9]+)?)\b""")

    // "08/20/25  591.69 MiB | 30.63 MiB | ..."
    val dailyLine = Regex(
        """(?m)^\s*(\d{2}/\d{2}/\d{2})\s+([0-9]+(?:\.[0-9]+)?)\s+([KMGT]?i?B)\s+\|\s+([0-9]+(?:\.[0-9]+)?)\s+([KMGT]?i?B)\b"""
    )
    @RequiresApi(Build.VERSION_CODES.O)
    val dateFmt = DateTimeFormatter.ofPattern("MM/dd/yy")

    fun parseHourly(raw: String): List<HourStat> {
        val found = mutableMapOf<Int, HourStat>()
        for (m in hourlyTriple.findAll(raw)) {
            val h  = m.groupValues[1].toInt()
            val rx = m.groupValues[2].toDouble()
            val tx = m.groupValues[3].toDouble()
            if (h in 0..23) found[h] = HourStat(h, rx, tx)
        }
        return (0..23).map { h -> found[h] ?: HourStat(h, 0.0, 0.0) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDaily(raw: String): List<DayStat> {
        val out = mutableListOf<DayStat>()
        for (m in dailyLine.findAll(raw)) {
            val date = LocalDate.parse(m.groupValues[1], dateFmt)
            val rx   = toMiB(m.groupValues[2].toDouble(), m.groupValues[3])
            val tx   = toMiB(m.groupValues[4].toDouble(), m.groupValues[5])
            out += DayStat(date, rx, tx)
        }
        return out.sortedBy { it.date }
    }

    private fun toMiB(value: Double, unit: String): Double = when (unit) {
        "KiB" -> value / 1024.0
        "MiB" -> value
        "GiB" -> value * 1024.0
        "TiB" -> value * 1024.0 * 1024.0
        else  -> value
    }
}

/* ---------- GRÁFICO DE LÍNEAS EN COMPOSE ---------- */
@Composable
fun LineChart(
    title: String,
    labels: List<String>,
    seriesA: List<Double>, // RX
    seriesB: List<Double>, // TX
    unitY: String = "MiB",
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(240.dp)
        .padding(horizontal = 12.dp, vertical = 8.dp)
) {
    val n = max(seriesA.size, seriesB.size)
    val maxYraw = max(seriesA.maxOrNull() ?: 0.0, seriesB.maxOrNull() ?: 0.0)
    val maxY = niceUpper(max(1.0, maxYraw))

    Column(Modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        Canvas(modifier) {
            val marginL = 56f
            val marginR = 12f
            val marginT = 16f
            val marginB = 28f
            val plotW = size.width - marginL - marginR
            val plotH = size.height - marginT - marginB
            val plotX = marginL
            val plotY = marginT

            // Fondo
            drawRect(color = Color(0xFFF8F9FC))

            // Grid Y + labels
            val ticks = 5
            for (i in 0..ticks) {
                val yVal = i * maxY / ticks
                val y = plotY + plotH - (yVal / maxY).toFloat() * plotH
                drawLine(Color(0xFFE6E8EE), Offset(plotX, y), Offset(plotX + plotW, y))
                drawContext.canvas.nativeCanvas.apply {
                    val text = "${yVal.roundToInt()} $unitY"
                    drawText(
                        text, plotX - 8f, y + 12f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.RIGHT
                        }
                    )
                }
            }
            // Eje X
            drawLine(Color(0xFF888888), Offset(plotX, plotY + plotH), Offset(plotX + plotW, plotY + plotH))

            if (n >= 1) {
                // helper para coord
                fun pt(i: Int, v: Double): Offset {
                    val x = if (n == 1) plotX else plotX + (i.toFloat() * (plotW / (n - 1f)))
                    val y = plotY + plotH - (v / maxY).toFloat() * plotH
                    return Offset(x, y)
                }

                // Etiquetas X (no todas)
                val step = when {
                    n <= 8 -> 1
                    n <= 16 -> 2
                    n <= 32 -> 3
                    else -> 4
                }
                for (i in labels.indices step step) {
                    val x = pt(i, 0.0).x
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            labels[i], x, plotY + plotH + 22f,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.DKGRAY
                                textSize = 28f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                    // guía vertical
                    drawLine(Color(0xFFE6E8EE), Offset(x, plotY), Offset(x, plotY + plotH),
                        strokeWidth = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f,6f)))
                }

                // Serie RX (sólida)
                if (seriesA.isNotEmpty()) {
                    val p = Path()
                    seriesA.forEachIndexed { i, v ->
                        val o = pt(i, v)
                        if (i == 0) p.moveTo(o.x, o.y) else p.lineTo(o.x, o.y)
                    }
                    drawPath(p, Color(0xFF8B5CF6), style = Stroke(width = 4f))
                }

                // Serie TX (discontinua)
                if (seriesB.isNotEmpty()) {
                    val p = Path()
                    seriesB.forEachIndexed { i, v ->
                        val o = pt(i, v)
                        if (i == 0) p.moveTo(o.x, o.y) else p.lineTo(o.x, o.y)
                    }
                    drawPath(
                        p, Color(0xFF10B981),
                        style = Stroke(width = 4f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f,10f)))
                    )
                }
            }
        }
    }
}

private fun niceUpper(v: Double): Double {
    val exp = floor(log10(v)).toInt()
    val frac = v / 10.0.pow(exp)
    val nf = when {
        frac <= 1 -> 1.0
        frac <= 2 -> 2.0
        frac <= 5 -> 5.0
        else -> 10.0
    }
    return nf * 10.0.pow(exp)
}

@RequiresApi(Build.VERSION_CODES.O)
fun reorderLast24h(stats: List<HourStat>): Pair<List<HourStat>, List<String>> {
    val now = LocalTime.now().hour
    val start = (now + 1) % 24
    val ordered = (0 until 24).map { stats[(start + it) % 24] }
    val labels  = (0 until 24).map { "%02d".format((start + it) % 24) }
    return ordered to labels
}

@RequiresApi(Build.VERSION_CODES.O)
fun normalizeLastNDays(days: List<DayStat>, n: Int = 30): List<DayStat> {
    val map = days.associateBy { it.date }
    val today = LocalDate.now()
    val start = today.minusDays((n - 1).toLong())
    return (0 until n).map { i ->
        val d = start.plusDays(i.toLong())
        map[d] ?: DayStat(d, rxMiB = 0.0, txMiB = 0.0)
    }
}

/* ---------- LEYENDA ---------- */
@Composable
private fun ChartLegend() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        LegendSwatch(Color(0xFF8B5CF6), dashed = false, label = "Descarga)")
        Spacer(Modifier.width(12.dp))
        LegendSwatch(Color(0xFF10B981), dashed = true,  label = "Subida)")
    }
}

@Composable
private fun LegendSwatch(color: Color, dashed: Boolean, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.width(28.dp).height(12.dp)) {
            drawLine(
                color = color,
                start = Offset(0f, size.height/2),
                end   = Offset(size.width, size.height/2),
                strokeWidth = 4f,
                pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(12f, 8f)) else null
            )
        }
        Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(start = 6.dp))
    }
}


/* ---------- PANTALLA PRINCIPAL ---------- */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VnstatMonitorScreen(
    provider: suspend () -> String,
) {
    var hours by remember { mutableStateOf(List(24) { HourStat(it, 0.0, 0.0) }) }
    var days  by remember { mutableStateOf(emptyList<DayStat>()) }

    LaunchedEffect(Unit) {
        while (true) {
            val raw = provider()
            hours = VnstatParser.parseHourly(raw)
            days  = VnstatParser.parseDaily(raw).takeLast(7)
            delay(60_000)
        }
    }

    val (hoursOrdered, hourLabels) = reorderLast24h(hours)
    val daysNorm = normalizeLastNDays(days, n = 7)
    val dayLabels  = daysNorm.map { it.date.toString().substring(5) }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        LineChart(
            title = "Últimas 24 h",
            labels = hourLabels,
            seriesA = hoursOrdered.map { it.rxMiB },
            seriesB = hoursOrdered.map { it.txMiB },
        )
        ChartLegend()
        Spacer(Modifier.height(8.dp))
        LineChart(
            title = "Últimos 7 días",
            labels = dayLabels,
            seriesA = daysNorm.map { it.rxMiB },
            seriesB = daysNorm.map { it.txMiB },
        )
        ChartLegend()
    }
}

/* ---------- PROVIDERS ---------- */
// Ejemplo de provider por SSH con JSch (ejecuta en el OpenWrt y trae stdout)
fun sshVnstatProvider(
    host: String,
    user: String,
    password: String,
    port: Int = 22
): suspend () -> String = {
    withContext(Dispatchers.IO) {
        val jsch = JSch()
        val session = jsch.getSession(user, host, port).apply {
            setPassword(password)
            setConfig("StrictHostKeyChecking", "no")
            connect(5000)
        }
        try {
            val cmd = "vnstat -h; echo; vnstat -d"
            val channel = session.openChannel("exec") as ChannelExec
            channel.setCommand(cmd)
            val input = channel.inputStream
            channel.connect(5000)
            val text = input.reader().readText()
            channel.disconnect()
            text
        } finally {
            session.disconnect()
        }
    }
}

// Provider de demo (texto estático)
suspend fun demoProvider(): String = SAMPLE

val SAMPLE = """
h  rx (MiB)   tx (MiB)  ][  h  rx (MiB)   tx (MiB)  ][  h  rx (MiB)   tx (MiB)
00 1.0 0.2 ][ 08 5.4 0.5 ][ 16 1.2 0.1
01 0.8 0.2 ][ 09 6.8 0.6 ][ 17 0.8 0.1
02 0.6 0.2 ][ 10 7.2 0.6 ][ 18 0.6 0.1
03 0.5 0.2 ][ 11 6.0 0.6 ][ 19 0.7 0.1
04 0.4 0.2 ][ 12 3.0 0.4 ][ 20 0.9 0.1
05 0.4 0.2 ][ 13 2.8 0.4 ][ 21 1.1 0.2
06 1.2 0.3 ][ 14 3.6 0.5 ][ 22 2.5 0.3
07 3.1 0.4 ][ 15 4.2 0.5 ][ 23 5.0 0.6

         day         rx      |     tx      |    total    |
      07/23/25      1.20 GiB |  110.00 MiB |    1.30 GiB |
      07/24/25    980.00 MiB |   90.00 MiB |    1.04 GiB |
      07/25/25      2.10 GiB |  180.00 MiB |    2.27 GiB |
      07/26/25    450.00 MiB |   40.00 MiB |  490.00 MiB |
      07/27/25    700.00 MiB |   55.00 MiB |  755.00 MiB |
      07/28/25      1.60 GiB |  120.00 MiB |    1.72 GiB |
      07/29/25      3.20 GiB |  250.00 MiB |    3.45 GiB |
      07/30/25    380.00 MiB |   35.00 MiB |  415.00 MiB |
      07/31/25      1.10 GiB |   85.00 MiB |    1.18 GiB |
      08/01/25    590.00 MiB |   45.00 MiB |  635.00 MiB |
      08/02/25      2.80 GiB |  210.00 MiB |    3.01 GiB |
      08/03/25    610.00 MiB |   48.00 MiB |  658.00 MiB |
      08/04/25    440.00 MiB |   41.00 MiB |  481.00 MiB |
      08/05/25      1.90 GiB |  140.00 MiB |    2.04 GiB |
      08/06/25    520.00 MiB |   44.00 MiB |  564.00 MiB |
      08/07/25    470.00 MiB |   43.00 MiB |  513.00 MiB |
      08/08/25      1.30 GiB |  100.00 MiB |    1.40 GiB |
      08/09/25      2.00 GiB |  160.00 MiB |    2.16 GiB |
      08/10/25    390.00 MiB |   38.00 MiB |  428.00 MiB |
      08/11/25    610.00 MiB |   52.00 MiB |  662.00 MiB |
      08/12/25    480.00 MiB |   46.00 MiB |  526.00 MiB |
      08/13/25      1.70 GiB |  130.00 MiB |    1.83 GiB |
      08/14/25    630.00 MiB |   50.00 MiB |  680.00 MiB |
      08/15/25      2.50 GiB |  190.00 MiB |    2.69 GiB |
      08/16/25    510.00 MiB |   49.00 MiB |  559.00 MiB |
      08/17/25    470.00 MiB |   42.00 MiB |  512.00 MiB |
      08/18/25      1.40 GiB |  120.00 MiB |    1.52 GiB |
      08/19/25    560.00 MiB |   47.00 MiB |  607.00 MiB |
      08/20/25    591.69 MiB |   30.63 MiB |  622.32 MiB |
""".trimIndent()
