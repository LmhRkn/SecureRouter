package com.tfg.securerouter.data.screens

import com.tfg.securerouter.data.router.sendCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ScreenComponentModelDefault {
    suspend fun loadData(): Boolean

    suspend fun <T> safeLoad(
        cache: MutableMap<String, Any>,
        command: String,
        cacheKey: String,
        parse: (String) -> T,
        setState: (T) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val output = sendCommand(command)
            val parsed = parse(output)

            cache[cacheKey] = output
            setState(parsed)

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
