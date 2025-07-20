package com.tfg.securerouter.data.router

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Executes a router command, parses its output, and optionally caches the result.
 *
 * Usage:
 * This function synchronously sends a command to the router, parses the response using
 * the provided [parse] lambda, and returns the parsed result via [onResult].
 * Optionally, it can store the raw output in a cache map using a [cacheKey].
 *
 * @param T The type of the parsed result.
 * @param command The command string to be sent to the router.
 * @param parse Lambda to parse the raw command output into type [T].
 * @param onResult Lambda called with the parsed result.
 * @param cache Optional mutable map for caching raw outputs.
 * @param cacheKey Optional key to use when storing the raw output in [cache].
 * @return `true` if the command was successfully executed and parsed; `false` otherwise.
 *
 * @see connectSSH
 * @see sendCommand
 */

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
