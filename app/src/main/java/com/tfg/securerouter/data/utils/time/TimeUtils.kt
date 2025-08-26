package com.tfg.securerouter.data.utils.time

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun blockedNowHuman(): String {
        val nowLocal = OffsetDateTime.now().atZoneSameInstant(ZoneId.systemDefault())
        val date = nowLocal.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))
        val time = nowLocal.format(DateTimeFormatter.ofPattern("HH:mm:ss", Locale("es", "ES")))
        return "el dia $date a las $time"
    }

    val knownTimeZones: Map<String, String> = mapOf(
        "Europe/Madrid" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/Paris" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/Berlin" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/Rome" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/London" to "GMT0BST,M3.5.0/1,M10.5.0/2",
        "UTC" to "UTC0"
    )
}