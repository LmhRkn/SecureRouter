package com.tfg.securerouter.data.app.screens.common.devices

import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.defaults.ScreenComponentModelDevicesDefault
import com.tfg.securerouter.data.app.screens.device_manager.state.HistoricalDeviceState
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class DevicesListModel<T>(
    open val sharedCache: MutableMap<String, Any>,
    private val createState: (List<DeviceModel>) -> T
) : ScreenComponentModelDevicesDefault {

    private val _state = MutableStateFlow(createState(emptyList()))
    open val state: StateFlow<T> = _state

    open override suspend fun loadData(): Boolean {
        return safeLoad(
            cache = sharedCache,
            command = "cat /tmp/dhcp.leases",
            cacheKey = "devices_output",
            parse = { parseDevices(it) },
            setState = { _state.value = createState(it) }
        )
    }

    fun parseDevices(output: String): List<DeviceModel> {
        return output.lines()
            .filter { it.isNotBlank() }
            .mapNotNull { line ->
                val parts = line.split(" ")
                if (parts.size < 5) return@mapNotNull null

                val mac = parts[1]
                val ip = parts[2]
                val hostName = parts[3]
                val dhcpIdentifier = parts[parts.size - 1]
                val statusFlag = "1"
                val blockedFlag = "1"
//                val statusFlag = parts[parts.size - 2]
//                val blockedFlag = parts[parts.size - 1]

                if (dhcpIdentifier == "*") return@mapNotNull null

                var deviceData = DeviceManagerCache.getDevice(mac)

                if (deviceData == null) {
                    deviceData = saveDiveJson(mac = mac, ip = ip, hostName = hostName, statusFlag = statusFlag, blockedFlag = blockedFlag)
                } else {
                    val deviceDataProv = updateLabels(mac = mac, ip = ip, statusFlag = statusFlag, blockedFlag = blockedFlag)
                    if (deviceDataProv != null) deviceData = deviceDataProv
                }

                if (deviceData.icon == null) {
                    val vendorName = getDeviceType(mac)
                    val (iconRes, iconDesc, extraLabel) = getDeviceIconAndType(vendorName)

                    val labels = deviceData.labels.toMutableSet()
                    extraLabel?.let { labels += it }

                    val updatedDevice = deviceData.copy(
                        ip = ip,
                        hostname = hostName,
                        icon = iconRes,
                        iconDescription = iconDesc,
                        labels = labels.toSet()
                    )

                    DeviceManagerCache.put(updatedDevice)
                    deviceData = updatedDevice
                }

                return@mapNotNull deviceData
            }
    }


    private fun saveDiveJson(mac: String, ip: String, hostName: String, statusFlag: String, blockedFlag: String): DeviceModel {
        val labels = mutableSetOf<DeviceLabel>()
        labels += if (statusFlag == "1") DeviceLabel.Online else DeviceLabel.Offline
        if (blockedFlag == "1") labels += DeviceLabel.Blocked

        val newDevice = DeviceModel(
            mac = mac,
            ip = ip,
            hostname = hostName,
            labels = labels
        )

        DeviceManagerCache.put(newDevice)
        return newDevice
    }

    private fun updateLabels(mac: String, ip: String, statusFlag: String, blockedFlag: String): DeviceModel? {
        val existing = DeviceManagerCache.getDevice(mac) ?: return null

        val currentLabels = existing.labels.toMutableSet()
        var changed = false

        val isOnline = statusFlag == "1"
        val alreadyOnline = DeviceLabel.Online in currentLabels
        val alreadyOffline = DeviceLabel.Offline in currentLabels

        if (isOnline && !alreadyOnline) {
            currentLabels -= DeviceLabel.Offline
            currentLabels += DeviceLabel.Online
            changed = true
        } else if (!isOnline && !alreadyOffline) {
            currentLabels -= DeviceLabel.Online
            currentLabels += DeviceLabel.Offline
            changed = true
        }

        val isBlocked = blockedFlag == "1"
        val alreadyBlocked = DeviceLabel.Blocked in currentLabels

        if (isBlocked && !alreadyBlocked) {
            currentLabels += DeviceLabel.Blocked
            changed = true
        } else if (!isBlocked && alreadyBlocked) {
            currentLabels -= DeviceLabel.Blocked
            changed = true
        }

        val updatedDevice = existing.copy(ip=ip, labels = currentLabels)
        DeviceManagerCache.put(updatedDevice)
        return updatedDevice
    }
}
