package com.tfg.securerouter.data.app.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.app.common.screen_components.DeviceLabel
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.router.sendCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.tfg.securerouter.R

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

    /**
     * Retrieves the vendor name for a given MAC address.
     *
     * This method first checks the [DeviceManagerCache] for cached results.
     * If not found, it queries an external API to resolve the vendor name.
     *
     * @param mac MAC address of the device.
     * @return Vendor name or `"Unknown"` if resolution fails.
     */
    fun getDeviceType(mac: String): String {
        DeviceManagerCache.get(mac)?.let {
            return it
        }

        val macParts = mac.split(":")
        if (macParts.size != 6) return "Unknown"

        val vendorName = fetchVendorFromApi(macParts)

        DeviceManagerCache.put(mac, vendorName)
        return vendorName
    }

    /**
     * Fetches the vendor name from the macvendors.com API.
     *
     * Performs up to 5 retries if rate-limited or if errors occur.
     *
     * @param macParts Split components of the MAC address.
     * @return Vendor name or `"Unknown"` if API resolution fails.
     */
    private fun fetchVendorFromApi(macParts: List<String>): String {
        val vendorMac = "${macParts[0]}:${macParts[1]}:${macParts[2]}:XX:XX:XX"
        val curlCommand = "curl -s https://api.macvendors.com/${vendorMac}"

        var vendorName = ""
        var attempt = 0
        val maxRetries = 5
        val retryDelayMs = 1000L

        while (attempt < maxRetries) {
            attempt++
            vendorName = sendCommand(curlCommand).trim()

            if (vendorName.contains("Too Many Requests", ignoreCase = true) ||
                vendorName.contains("Please slow down", ignoreCase = true)
            ) {
                Thread.sleep(retryDelayMs)
                continue
            }

            if (vendorName.isNotBlank() && !vendorName.contains("errors", ignoreCase = true)) {
                break
            }

            Thread.sleep(retryDelayMs)
        }

        return if (vendorName.isBlank() || vendorName.contains("errors", ignoreCase = true)) {
            "Unknown"
        } else {
            vendorName
        }
    }

    /**
     * Resolves an icon, description, and optional [DeviceLabel] based on the vendor name.
     *
     * Categorizes devices into:
     * - 📱 Phones
     * - 💻 PCs
     * - 🎮 Consoles
     * - 📺 Other IoT devices
     * - 🌐 Default fallback
     *
     * @param vendorName The vendor name resolved from the MAC address.
     * @return Triple of [ImageVector], description resource ID, and optional [DeviceLabel].
     */
    fun getDeviceIconAndType(vendorName: String): Triple<ImageVector, Int, DeviceLabel?> {
        val vendor = vendorName.lowercase()

        return when {
            vendor.contains("apple") ||
                    vendor.contains("samsung") ||
                    vendor.contains("huawei") ||
                    vendor.contains("xiaomi") ||
                    vendor.contains("oneplus") ||
                    vendor.contains("google") ||
                    vendor.contains("oppo") ||
                    vendor.contains("vivo")
                        -> Triple(Icons.Filled.PhoneAndroid, R.string.device_phone_icon, DeviceLabel.Phone)

            vendor.contains("dell") ||
                    vendor.contains("hp") ||
                    vendor.contains("lenovo") ||
                    vendor.contains("asus") ||
                    vendor.contains("msi") ||
                    vendor.contains("acer") ||
                    vendor.contains("gigabyte") ||
                    vendor.contains("microsoft")
                        -> Triple(Icons.Filled.Laptop, R.string.device_pc_icon, DeviceLabel.PC)

            vendor.contains("sony") ||
                    vendor.contains("nintendo") ||
                    vendor.contains("microsoft")
                        -> Triple(Icons.Filled.SportsEsports, R.string.device_console_icon, DeviceLabel.Console)

            vendor.contains("lg") ||
                    vendor.contains("philips") ||
                    vendor.contains("hikvision") ||
                    vendor.contains("tp-link") ||
                    vendor.contains("tplink") ||
                    vendor.contains("raspberry") ||
                    vendor.contains("amazon") ||
                    vendor.contains("roku")
                        -> Triple(Icons.Filled.DevicesOther, R.string.device_other_device_icon, null)

            else -> Triple(Icons.Filled.DevicesOther, R.string.device_other_device_icon, null)
        }
    }
}
