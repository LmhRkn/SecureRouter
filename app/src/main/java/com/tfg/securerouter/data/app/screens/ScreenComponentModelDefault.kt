package com.tfg.securerouter.data.app.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.tfg.securerouter.data.router.sendCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Base interface for all screen component models in the app.
 *
 * Responsibilities:
 * - Defines a common contract for data loading via [loadData].
 * - Provides utility methods for safe command execution and parsing ([safeLoad]).
 * - Includes helper functions to resolve vendor names, device types, and icons from MAC addresses.
 */
interface ScreenComponentModelDefault {
    /**
     * Loads the necessary data for the implementing screen component.
     *
     * This method should be implemented by all concrete screen models to
     * fetch and parse their respective data from the router or other sources.
     *
     * @return `true` if the data loaded successfully, `false` otherwise.
     */
    suspend fun loadData(): Boolean

    /**
     * Executes a command safely in the background and updates the provided state.
     *
     * @param cache Shared in-memory cache to avoid redundant command execution.
     * @param command Shell command to execute on the router.
     * @param cacheKey Key used to store/retrieve the raw output from [cache].
     * @param parse Lambda to transform raw command output into the desired type [T].
     * @param setState Lambda to update the screen’s state with the parsed result.
     *
     * @return `true` if the command executed and parsed successfully, `false` otherwise.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun <T> safeLoad(
        cache: MutableMap<String, Any>,
        command: String,
        cacheKey: String,
        parse: (String) -> T,
        setState: (T) -> Unit
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val output: String = sendCommand(command)
            Log.d("HomeRouterInfoModel", "Entró: $output")
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

