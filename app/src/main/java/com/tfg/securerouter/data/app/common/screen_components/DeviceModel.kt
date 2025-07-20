package com.tfg.securerouter.data.app.common.screen_components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data class representing a device connected to the router.
 *
 * Used to model entries parsed from the router's DHCP leases file (/tmp/dhcp.leases).
 *
 * Usage:
 * - Displayed in device lists on the Home screen.
 * - Can be extended in the future to include connection time, vendor, etc.
 *
 * @property mac The MAC address [String] ]of the connected device.
 * @property hostname The hostname [String] of the device, if available (can be Null)
 * @property ip The IP address [String] assigned to the device by the router.
 * @property icon The [ImageVector] representing the device's type.
 * @property iconDescription A human-readable description of the device's icon.
 * @property labels A set of [DeviceLabel]s associated with the device.
 *
 * @see DeviceLabel
 */

data class DeviceModel(
    val mac: String,
    val hostname: String?,
    val ip: String,
    val icon: ImageVector,
    val iconDescription: Int,
    val labels: Set<DeviceLabel> = emptySet()
)