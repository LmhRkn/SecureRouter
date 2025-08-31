@file:Suppress("unused")

package com.tfg.securerouter.data.app.screens.wifi.utils.graph

import android.graphics.Paint
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

data class HourStat(val hour: Int, val rxMiB: Double, val txMiB: Double)
data class DayStat(val date: LocalDate, val rxMiB: Double, val txMiB: Double)

object VnstatParser {
    val hourlyTriple =
        Regex("""\b(\d{2})\s+([0-9]+(?:\.[0-9]+)?)\s+([0-9]+(?:\.[0-9]+)?)\b""")

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

@Composable
fun LineChart(
    title: String,
    labels: List<String>,
    seriesA: List<Double>,
    seriesB: List<Double>,
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

            drawRect(color = Color(0xFFF8F9FC))

            val ticks = 5
            for (i in 0..ticks) {
                val yVal = i * maxY / ticks
                val y = plotY + plotH - (yVal / maxY).toFloat() * plotH
                drawLine(Color(0xFFE6E8EE), Offset(plotX, y), Offset(plotX + plotW, y))
                drawContext.canvas.nativeCanvas.apply {
                    val text = "${yVal.roundToInt()} $unitY"
                    drawText(
                        text, plotX - 8f, y + 12f,
                        Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = 28f
                            textAlign = Paint.Align.RIGHT
                        }
                    )
                }
            }
            drawLine(Color(0xFF888888), Offset(plotX, plotY + plotH), Offset(plotX + plotW, plotY + plotH))

            if (n >= 1) {
                fun pt(i: Int, v: Double): Offset {
                    val x = if (n == 1) plotX else plotX + (i.toFloat() * (plotW / (n - 1f)))
                    val y = plotY + plotH - (v / maxY).toFloat() * plotH
                    return Offset(x, y)
                }

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
                            Paint().apply {
                                color = android.graphics.Color.DKGRAY
                                textSize = 28f
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                    drawLine(Color(0xFFE6E8EE), Offset(x, plotY), Offset(x, plotY + plotH),
                        strokeWidth = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f,6f)))
                }

                if (seriesA.isNotEmpty()) {
                    val p = Path()
                    seriesA.forEachIndexed { i, v ->
                        val o = pt(i, v)
                        if (i == 0) p.moveTo(o.x, o.y) else p.lineTo(o.x, o.y)
                    }
                    drawPath(p, Color(0xFF8B5CF6), style = Stroke(width = 4f))
                }

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
            val cmd = "vnstat -u; vnstat -h; echo; vnstat -d"
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