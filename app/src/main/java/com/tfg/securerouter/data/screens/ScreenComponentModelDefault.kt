package com.tfg.securerouter.data.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.common.screen_components.DeviceLabel
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

    fun getDeviceType(mac: String): String {
        val macParts = mac.split(":")
        if (macParts.size != 6) return "Unknown"

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

        if (vendorName.isBlank() || vendorName.contains("errors", ignoreCase = true)) {
            vendorName = "Unknown"
        }

        return vendorName
    }

    fun getDeviceIconAndType(vendorName: String): Pair<ImageVector, DeviceLabel?> {
        val vendor = vendorName.lowercase()

        return when {
            // 📱 Dispositivos móviles
            vendor.contains("apple") ||
                    vendor.contains("samsung") ||
                    vendor.contains("huawei") ||
                    vendor.contains("xiaomi") ||
                    vendor.contains("oneplus") ||
                    vendor.contains("google") ||
                    vendor.contains("oppo") ||
                    vendor.contains("vivo") -> Pair(Icons.Filled.PhoneAndroid, DeviceLabel.Phone)

            // 💻 PCs, portátiles y servidores
            vendor.contains("dell") ||
                    vendor.contains("hp") ||
                    vendor.contains("lenovo") ||
                    vendor.contains("asus") ||
                    vendor.contains("msi") ||
                    vendor.contains("acer") ||
                    vendor.contains("gigabyte") ||
                    vendor.contains("microsoft") -> Pair(Icons.Filled.Laptop, DeviceLabel.PC)

            // 🎮 Consolas de juegos
            vendor.contains("sony") ||
                    vendor.contains("nintendo") ||
                    vendor.contains("microsoft") -> Pair(Icons.Filled.SportsEsports, DeviceLabel.Console)

            // 📺 Otros (TV, IoT, impresoras, cámaras…)
            vendor.contains("lg") ||
                    vendor.contains("philips") ||
                    vendor.contains("hikvision") ||
                    vendor.contains("tp-link") ||
                    vendor.contains("tplink") ||
                    vendor.contains("raspberry") ||
                    vendor.contains("amazon") ||
                    vendor.contains("roku") -> Pair(Icons.Filled.DevicesOther, null)

            // 🌐 Default (sin coincidencia)
            else -> Pair(Icons.Filled.DevicesOther, null)
        }
    }
}
