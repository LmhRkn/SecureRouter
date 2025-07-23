package com.tfg.securerouter.data.app.common.screen_components.devices

import com.tfg.securerouter.R
import com.tfg.securerouter.ui.app.common.searchbar.FilterButton

/**
 * Enum class representing different device types or states for labeling UI elements.
 *
 * Usage:
 * Each enum value corresponds to a category or status of a device (e.g., Phone, PC, Online).
 * It includes a [displayName] property that holds a reference to a localized string resource.
 * This is used in UI components to display user-friendly labels.
 *
 * Enum Values:
 * - [Phone]: Represents mobile devices.
 * - [PC]: Represents personal computers.
 * - [Console]: Represents gaming consoles.
 * - [Online]: Represents devices currently connected.
 * - [Offline]: Represents devices currently disconnected.
 * - [Blocked]: Represents devices that are blocked by the router.
 *
 * @property displayName The string resource ID (as String) for the label's display name.
 *
 * @see FilterButton
 */

enum class DeviceLabel(val displayName: String) {
    Phone(R.string.device_label_phone.toString()),
    PC(R.string.device_label_pc.toString()),
    Console(R.string.device_label_console.toString()),
    Online(R.string.device_label_online.toString()),
    Offline(R.string.device_label_offline.toString()),
    Blocked(R.string.device_label_blocked.toString());

    override fun toString(): String = displayName
}
