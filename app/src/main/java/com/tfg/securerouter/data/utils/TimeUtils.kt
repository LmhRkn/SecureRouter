package com.tfg.securerouter.data.utils

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatBlockedAt(iso: String): String = try {
        val zdt = OffsetDateTime.parse(iso, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            .atZoneSameInstant(ZoneId.systemDefault())
        val date = zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale("es", "ES")))
        val time = zdt.format(DateTimeFormatter.ofPattern("HH:mm:ss", Locale("es", "ES")))
        "el dia $date a las $time"
    } catch (_: Exception) {
        "Bloqueado el $iso"
    }
}
