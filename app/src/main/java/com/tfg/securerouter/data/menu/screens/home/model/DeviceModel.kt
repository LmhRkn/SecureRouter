package com.tfg.securerouter.data.menu.screens.home.model

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
 */

data class DeviceModel(
    val mac: String,
    val hostname: String?,
    val ip: String
)