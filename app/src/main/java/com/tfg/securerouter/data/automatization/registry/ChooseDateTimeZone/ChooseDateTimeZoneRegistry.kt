package com.tfg.securerouter.data.automatization.registry.ChooseDateTimeZone

object ChooseDateTimeZoneRegistry {
    val knownTimeZones: Map<String, String> = mapOf(
        "Europe/Madrid" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/Paris" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/Berlin" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/Rome" to "CET-1CEST,M3.5.0,M10.5.0/3",
        "Europe/London" to "GMT0BST,M3.5.0/1,M10.5.0/2",
        "UTC" to "UTC0"
    )
}