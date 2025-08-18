// com/tfg/securerouter/data/automatization/automatizations/before_opening/ChooseDateTimeZone.kt
package com.tfg.securerouter.data.automatization.automatizations.before_opening

import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import java.util.TimeZone

class ChooseDateTimeZone(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    private fun desiredZoneName(): String = TimeZone.getDefault().id // p.ej. "Europe/Madrid"

    private suspend fun currentZoneName(): String =
        sh("uci -q get system.@system[0].zonename 2>/dev/null || echo ''").trim()

    private suspend fun currentPosix(): String =
        sh("uci -q get system.@system[0].timezone 2>/dev/null || echo ''").trim()

    override suspend fun shouldRun(): Boolean {
        val desired = desiredZoneName()
        if (desired.isBlank()) return false
        val curName = currentZoneName()
        if (desired != curName) return true

        // Si el zonename ya coincide, verifica que el POSIX sea el esperado
        val expectedPosix = buildPosixForZone(desired)
        val curPosix = currentPosix()
        return expectedPosix.isNotBlank() && expectedPosix != curPosix
    }

    override suspend fun execute(): Boolean {
        val desired = desiredZoneName()
        if (desired.isBlank()) return false

        val posix = buildPosixForZoneAuto(desired)

        val script = buildString {
            appendLine("uci -q set system.@system[0].zonename='$desired'")
            if (posix != null) {
                appendLine("uci -q set system.@system[0].timezone='$posix'")
            } // si es null, no tocamos 'timezone' para no romper DST
            appendLine("uci -q commit system")
            appendLine("/etc/init.d/system reload 2>/dev/null || /etc/init.d/system reload_config 2>/dev/null || /etc/init.d/system restart || true")
            appendLine("/etc/init.d/sysntpd restart 2>/dev/null || true")
            appendLine("uci -q get system.@system[0].zonename 2>/dev/null || echo ''")
        }

        val after = sh(script).trim().lines().lastOrNull().orEmpty()
        val okName = after == desired
        val okPosix = posix?.let { sh("uci -q get system.@system[0].timezone 2>/dev/null || echo ''").trim() == it } ?: true
        return okName && okPosix
    }


    /** Construye la cadena POSIX para la zona deseada. */
    private suspend fun buildPosixForZone(desired: String): String {
        // 1) Intentar tzdata (OpenWrt: /usr/share/zoneinfo/tzdata)
        val tzPosix: String = sh(
            """
            if [ -f /usr/share/zoneinfo/tzdata ]; then
              awk -v z='$desired' '${'$'}1==z {print ${'$'}2; exit}' /usr/share/zoneinfo/tzdata
            else
              echo ""
            fi
            """.trimIndent()
        ).trim()
        if (tzPosix.isNotBlank()) return tzPosix

        // 2) Mapa de fallbacks “buenos” para zonas comunes
        val known = mapOf(
            "Europe/Madrid" to "CET-1CEST,M3.5.0,M10.5.0/3",
            "Europe/Paris" to "CET-1CEST,M3.5.0,M10.5.0/3",
            "Europe/Berlin" to "CET-1CEST,M3.5.0,M10.5.0/3",
            "Europe/Rome" to "CET-1CEST,M3.5.0,M10.5.0/3",
            "Europe/London" to "GMT0BST,M3.5.0/1,M10.5.0/2",
            "UTC" to "UTC0"
        )
        known[desired]?.let { return it }

        // 3) Último recurso: derivar desde offset actual (no contempla cambios estacionales)
        val raw = sh("date +%z 2>/dev/null || echo +0000").trim() // ej: +0200
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
        suspend fun readFromTzdata(): String =
            sh(
                """
            if [ -f /usr/share/zoneinfo/tzdata ]; then
              awk -v z='$desired' '${'$'}1==z {print ${'$'}2; exit}' /usr/share/zoneinfo/tzdata
            else
              echo ""
            fi
            """.trimIndent()
            ).trim()

        readFromTzdata().takeIf { it.isNotBlank() }?.let { return it }

        val rid = AppSession.routerId?.toString()
        val pm = AppSession.packageInstaller
            ?: (rid?.let { RouterSelectorCache.getRouter(it)?.installerPackage })

        when (pm) {
            "opkg" -> {
                val region = desired.substringBefore('/', "").lowercase()
                val extra = if (region.isNotEmpty()) " zoneinfo-$region" else ""
                sh("opkg update >/dev/null 2>&1 || true; opkg install zoneinfo-core$extra >/dev/null 2>&1 || true")
            }
            "apk" -> {
                sh("apk add --no-cache tzdata >/dev/null 2>&1 || true")
            }
        }

        readFromTzdata().takeIf { it.isNotBlank() }?.let { return it }

        return null
    }

}
