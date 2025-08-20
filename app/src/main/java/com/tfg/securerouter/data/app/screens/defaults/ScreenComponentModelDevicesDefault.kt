package com.tfg.securerouter.data.app.screens.defaults

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.ui.graphics.vector.ImageVector
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.router.sendCommand
import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.common.screen_components.devices.deviceTypes
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.toDeviceModel
import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault


interface ScreenComponentModelDevicesDefault: ScreenComponentModelDefault {

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
        val macParts = mac.split(":")
        if (macParts.size != 6) return "Unknown"

        val vendorName = fetchVendorFromApi(macParts)
        val (icon, descriptionRes, label) = getDeviceIconAndType(vendorName)

        val existingDevice = DeviceManagerCache.getDevice(mac)

        if (existingDevice != null) {
            val updatedDevice = existingDevice.copy(
                icon = icon,
                iconDescription = descriptionRes,
                labels = existingDevice.labels + listOfNotNull(label)
            )
            DeviceManagerCache.put(updatedDevice)
        }
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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

    fun getDeviceIconAndType(vendorName: String): Triple<ImageVector, Int, DeviceLabel?> {
        val vendor = vendorName.lowercase()

        val deviceTypes = deviceTypes

        for (deviceType in deviceTypes) {
            if (deviceType.keywords.any { vendor.contains(it) }) {
                return Triple(deviceType.icon, deviceType.descriptionRes, deviceType.label)
            }
        }

        return Triple(deviceTypes.last().icon, deviceTypes.last().descriptionRes, deviceTypes.last().label)
    }
}

