package com.tfg.securerouter.data.router

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun <T> sendAndParse(
    command: String,
    parse: (String) -> T,
    onResult: (T) -> Unit,
    cache: MutableMap<String, Any>? = null,
    cacheKey: String? = null
): Boolean {
    return try {
        val output = sendCommand(command)

        cache?.let { c -> cacheKey?.let { key -> c[key] = output } }

        val parsed = parse(output)
        onResult(parsed)

        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun <T> launchCommand(
    command: String,
    parse: (String) -> T,
    onResult: (T) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        sendAndParse(command, parse, onResult)
    }
}
