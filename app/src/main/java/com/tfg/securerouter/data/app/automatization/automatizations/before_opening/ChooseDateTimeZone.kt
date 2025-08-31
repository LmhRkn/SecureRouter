package com.tfg.securerouter.data.app.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.time.TimeUtils
import java.util.TimeZone

class ChooseDateTimeZone(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 60_000L

    private fun desiredZoneName(): String = TimeZone.getDefault().id

    private fun isLikelyPosixTz(s: String): Boolean {
        if (s.isBlank()) return false
        if (s.contains(' ')) return false
        if (s.length > 64) return false
        val allowed = s.all { it.isLetterOrDigit() || it in setOf('+','-','.',',',':','/','_') }
        val hasTzShape = s.any { it.isDigit() } || s.contains(',') || s.contains('+') || s.contains('-')
        return allowed && hasTzShape
    }

    private fun sanitizeToLastValidPosix(out: String): String? =
        out.lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()
            .asReversed()
            .firstOrNull { isLikelyPosixTz(it) }

    private fun sanitizeToLastNonEmpty(out: String): String =
        out.lineSequence().map { it.trim() }.lastOrNull { it.isNotEmpty() }.orElse("")

    private suspend fun shLastLine(cmd: String): String =
        sanitizeToLastNonEmpty(sh("( { $cmd ; } 2>/dev/null || true) | tail -n 1"))

    private suspend fun currentZoneName(): String =
        shLastLine("uci -q get system.@system[0].zonename || echo ''")

    private suspend fun currentPosix(): String =
        shLastLine("uci -q get system.@system[0].timezone || echo ''")

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val desired = desiredZoneName()
        if (desired.isBlank()) return 0

        val curName = currentZoneName()
        val expectedPosix = buildPosixForZone(desired)
        val curPosix = currentPosix()

        return if (curName != desired) 1
        else if (expectedPosix.isNotBlank() && expectedPosix != curPosix) 1
        else -1
    }

    override suspend fun execute(): Boolean {
        val desired = desiredZoneName()
        if (desired.isBlank()) return false

        val posixUnsafe = buildPosixForZoneAuto(desired)
        val posix = posixUnsafe?.takeIf { isLikelyPosixTz(it) }
        if (posixUnsafe != null && posix == null) {
            Log.w("ChooseDateTimeZone", "POSIX inválido capturado: [$posixUnsafe], se omite.")
        }

        val script = buildString {
            appendLine("uci -q set system.@system[0].zonename='$desired'")
            if (posix != null) {
                appendLine("uci -q set system.@system[0].timezone='$posix'")
            }
            appendLine("uci -q commit system")
            appendLine("/etc/init.d/system reload 2>/dev/null || /etc/init.d/system reload_config 2>/dev/null || /etc/init.d/system restart || true")
            appendLine("/etc/init.d/sysntpd restart 2>/dev/null || true")
            appendLine("uci -q get system.@system[0].zonename 2>/dev/null || echo ''")
        }

        val afterName = sanitizeToLastNonEmpty(sh(script).trim())
        val okName = afterName == desired
        val okPosix = posix?.let {
            val got = sanitizeToLastValidPosix(
                sh("uci -q get system.@system[0].timezone 2>/dev/null || echo ''")
            )
            got == it
        } ?: true

        return okName && okPosix
    }

    private suspend fun buildPosixForZone(desired: String): String {
        TimeUtils.knownTimeZones[desired]?.let { return it }

        val tzPosixRaw = sh(
            """
            if [ -f /usr/share/zoneinfo/tzdata ]; then
              awk -v z='$desired' '${'$'}1==z {print ${'$'}2; exit}' /usr/share/zoneinfo/tzdata
            else
              echo ""
            fi
            """.trimIndent()
        )
        val tzPosix = sanitizeToLastValidPosix(tzPosixRaw)
        if (!tzPosix.isNullOrBlank()) return tzPosix

        val raw = sanitizeToLastNonEmpty(sh("date +%z 2>/dev/null || echo +0000")).trim() // ej: +0200
        val m = Regex("""([+-])(\d{2})(\d{2})""").matchEntire(raw)
        return if (m != null) {
            val sign = if (m.groupValues[1] == "+") "-" else "+"
            val hh = m.groupValues[2].toInt()
            val mm = m.groupValues[3].toInt()
            if (mm == 0) "UTC$sign$hh" else "UTC$sign$hh:$mm"
        } else {
            "UTC0"
        }
    }

    private suspend fun buildPosixForZoneAuto(desired: String): String? {
        TimeUtils.knownTimeZones[desired]?.let { return it }

        suspend fun readFromTzdata(): String? {
            val out = sh(
                """
                if [ -f /usr/share/zoneinfo/tzdata ]; then
                  awk -v z='$desired' '$1==z {print $2; exit}' /usr/share/zoneinfo/tzdata
                else
                  echo ""
                fi
                """.trimIndent()
            )
            return sanitizeToLastValidPosix(out)
        }

        readFromTzdata()?.let { return it }

        val rid = AppSession.routerId?.toString()
        val pm = AppSession.packageInstaller
            ?: (rid?.let { RouterSelectorCache.getRouter(it)?.installerPackage })

        when (pm) {
            "opkg" -> {
                val region = desired.substringBefore('/', "").lowercase()
                val extra = if (region.isNotEmpty()) " zoneinfo-$region" else ""
                sh("(opkg update >/dev/null 2>&1 || true; opkg install zoneinfo-core$extra >/dev/null 2>&1 || true) >/dev/null 2>&1 || true")
            }
            "apk" -> {
                sh("(apk add --no-cache tzdata >/dev/null 2>&1 || true) >/dev/null 2>&1 || true")
            }
        }

        readFromTzdata()?.let { return it }

        return null
    }
    private fun String?.orElse(fallback: String) = this ?: fallback
}
