package com.tfg.securerouter.data.app.screens.common.devices

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.tfg.securerouter.data.app.common.screen_components.devices.DeviceLabel
import com.tfg.securerouter.data.app.common.screen_components.devices.model.DeviceModel
import com.tfg.securerouter.data.app.screens.defaults.ScreenComponentModelDevicesDefault
import com.tfg.securerouter.data.json.device_manager.DeviceManagerCache
import com.tfg.securerouter.data.utils.TimeUtils.blockedNowHuman
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Presencia simple SIN ARP:
 * - /tmp/dhcp.leases  -> identidad + expiración
 * - bridge fdb/brctl  -> presencia L2 (cableado y Wi-Fi bridged)
 * - uci wireless      -> MACs baneadas (macfilter='deny' en wifi-iface)
 *
 * Online := (MAC presente en FDB)
 * Blocked := (MAC listada en maclist de alguna wifi-iface con macfilter='deny')
 *
 * Orden: New -> Online -> resto (alfabético por hostname/ip dentro de cada grupo).
 */
open class DevicesListModel<T>(
    open val sharedCache: MutableMap<String, Any>,
    private val createState: (List<DeviceModel>) -> T
) : ScreenComponentModelDevicesDefault {

    private val _state = MutableStateFlow(createState(emptyList()))
    open val state: StateFlow<T> = _state

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun loadData(): Boolean {
        val bundleCmd = """
            (
              echo '===LEASES==='
              cat /tmp/dhcp.leases 2>/dev/null || true

              echo '===FDB==='
              (bridge fdb show 2>/dev/null || brctl showmacs br-lan 2>/dev/null || true)

              echo '===BLOCKED==='
              for s in ${'$'}(uci -q show wireless | sed -n "s/^wireless\.\\([^=]*\\)=wifi-iface/\\1/p"); do
                list=${'$'}(uci -q get wireless.${'$'}s.maclist 2>/dev/null)
                [ -n "${'$'}list" ] || continue
                echo "${'$'}list" | tr ' ' '\n'
              done
            ) | tr -d '\r'
        """.trimIndent()

        return safeLoad(
            cache = sharedCache,
            command = bundleCmd,
            cacheKey = "devices_bundle_with_blocked",
            parse = { parseDevicesBundle(it) },
            setState = { list -> _state.value = createState(sortDevices(list)) }
        )
    }

    private fun sortDevices(list: List<DeviceModel>): List<DeviceModel> {
        fun rank(d: DeviceModel): Int = when {
            DeviceLabel.New in d.labels    -> 0
            DeviceLabel.Online in d.labels -> 1
            else                           -> 2
        }
        return list.sortedWith(
            compareBy<DeviceModel> { rank(it) }
                .thenBy { (it.hostname ?: it.ip).lowercase() }
        )
    }

    private data class PresenceSnapshot(val fdbMacs: Set<String>) {
        fun isOnline(mac: String) = mac.lowercase() in fdbMacs
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDevicesBundle(bundle: String): List<DeviceModel> {
        val leases = extractSection(bundle, "===LEASES===", "===FDB===")
        val fdb    = extractSection(bundle, "===FDB===", "===BLOCKED===")
        val blockedSection = extractSection(bundle, "===BLOCKED===", null)

        val presence = PresenceSnapshot(fdbMacs = parseBridgeFdb(fdb))
        val blocked  = parseBlockedMacs(blockedSection)

        val fromRouter = parseDhcpLeasesWithPresence(leases, presence, blocked)
        return mergeWithCached(fromRouter, presence, blocked)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mergeWithCached(
        current: List<DeviceModel>,
        presence: PresenceSnapshot,
        blockedMacs: Set<String>
    ): List<DeviceModel> {
        val currentMacs = current.map { it.mac.lowercase() }.toSet()

        val extras = DeviceManagerCache.getAllDevices()
            .filter { it.mac.lowercase() !in currentMacs }
            .map { cached ->
                val macL = cached.mac.lowercase()
                val isOnline = presence.isOnline(macL)
                val isBlocked = macL in blockedMacs

                val labels = cached.labels.toMutableSet().apply {
                    if (isOnline) { add(DeviceLabel.Online); remove(DeviceLabel.Offline) }
                    else { add(DeviceLabel.Offline); remove(DeviceLabel.Online) }

                    if (isBlocked) add(DeviceLabel.Blocked) else remove(DeviceLabel.Blocked)
                }.toSet()

                val enriched = if (cached.icon == null) {
                    val vendorName = getDeviceType(macL)
                    val (iconRes, iconDesc, extraLabel) = getDeviceIconAndType(vendorName)
                    val newLabels = labels.toMutableSet().apply { extraLabel?.let { add(it) } }.toSet()
                    cached.copy(icon = iconRes, iconDescription = iconDesc, labels = newLabels)
                        .also { DeviceManagerCache.put(it) }
                } else {
                    cached.copy(labels = labels).also { DeviceManagerCache.put(it) }
                }

                enriched
            }

        return current + extras
    }

    private fun extractSection(text: String, startMarker: String, endMarker: String?): String {
        val start = text.indexOf(startMarker)
        if (start == -1) return ""
        val from = start + startMarker.length
        val end = endMarker?.let { m -> text.indexOf(m, from) } ?: -1
        return if (end == -1) text.substring(from) else text.substring(from, end)
    }

    private fun parseBridgeFdb(fdbText: String): Set<String> {
        val res = mutableSetOf<String>()
        val lines = fdbText.lines()

        // Formato "brctl showmacs br-lan" (como el dump que enviaste)
        val brctlRe = Regex("""^\s*\d+\s+([0-9a-fA-F:]{17})\s+(yes|no)\s+([\d.]+)""", RegexOption.IGNORE_CASE)
        var matchedBrctl = false
        for (l in lines) {
            val m = brctlRe.find(l) ?: continue
            matchedBrctl = true
            val mac = m.groupValues[1].lowercase()
            val isLocal = m.groupValues[2].equals("yes", ignoreCase = true)
            if (!isLocal) res += mac // solo hosts reales
        }
        if (matchedBrctl) return res

        // Formato "bridge fdb show"
        val macRe = Regex("""\b([0-9a-fA-F]{2}(?::[0-9a-fA-F]{2}){5})\b""")
        lines.forEach { l ->
            val line = l.trim()
            if (line.isEmpty()) return@forEach
            if (line.contains(" self ") || line.contains(" permanent") || line.contains(" local")) return@forEach
            macRe.findAll(line).forEach { res += it.groupValues[1].lowercase() }
        }
        return res
    }

    private fun parseBlockedMacs(blockedText: String): Set<String> {
        val macRe = Regex("""^['"]?([0-9a-fA-F]{2}(?::[0-9a-fA-F]{2}){5})['"]?$""")
        return blockedText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { line -> macRe.find(line)?.groupValues?.get(1)?.lowercase() }
            .toSet()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDhcpLeasesWithPresence(
        leasesText: String,
        presence: PresenceSnapshot,
        blockedMacs: Set<String>
    ): List<DeviceModel> {
        val now = System.currentTimeMillis() / 1000L

        return leasesText.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { line ->
                // OpenWrt: <expiry> <mac> <ip> <hostname> <clientid>
                val parts = line.split(Regex("\\s+"))
                if (parts.size < 5) return@mapNotNull null

                val expiry = parts[0].toLongOrNull() ?: 0L
                val mac = parts[1].lowercase()
                val ip = parts[2]
                val hostName = parts[3]
                val dhcpIdentifier = parts.last()
                if (dhcpIdentifier == "*") return@mapNotNull null

                val isLeaseValid = (expiry == 0L) || (expiry > now)   // 0 => estático
                val isOnlineNow = presence.isOnline(mac)
                val isBlockedNow = mac in blockedMacs

                val statusFlag = if (isOnlineNow) "1" else "0"
                val blockedFlag = if (isBlockedNow) "1" else "0"

                val device: DeviceModel =
                    DeviceManagerCache.getDevice(mac)
                        ?.let { existing -> updateLabels(mac, ip, statusFlag, blockedFlag) ?: existing }
                        ?: saveDiveJson(mac, ip, hostName, statusFlag, blockedFlag)


                val enriched: DeviceModel =
                    if (device.icon == null) {
                        val vendorName = getDeviceType(mac)
                        val (iconRes, iconDesc, extraLabel) = getDeviceIconAndType(vendorName)
                        Log.d("DevicesListModel", "iconRes: $iconRes, iconDesc: $iconDesc, extraLabel: $extraLabel")
                        val labels = device.labels.toMutableSet().apply { extraLabel?.let { add(it) } }.toSet()

                        device.copy(
                            ip = ip,
                            hostname = hostName,
                            icon = iconRes,
                            iconDescription = iconDesc,
                            labels = labels
                        ).also { DeviceManagerCache.put(it) }
                    } else device

                val finalDevice: DeviceModel =
                    if (!isLeaseValid && !isOnlineNow) {
                        enriched.copy(
                            labels = enriched.labels - DeviceLabel.Online + DeviceLabel.Offline
                        ).also { DeviceManagerCache.put(it) }
                    } else enriched
                // -----------------------------------------------------

                finalDevice
            }
    }

    // ---------- Persistencia de labels ----------
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDiveJson(
        mac: String,
        ip: String,
        hostName: String,
        statusFlag: String,
        blockedFlag: String
    ): DeviceModel {
        val labels = mutableSetOf<DeviceLabel>()
        val blockAt: String? = if (blockedFlag == "1") "Bloqueado antes d${blockedNowHuman()}" else null
        labels += if (statusFlag == "1") DeviceLabel.Online else DeviceLabel.Offline
        if (blockedFlag == "1") labels += DeviceLabel.Blocked

        labels += DeviceLabel.New

        val newDevice = DeviceModel(
            mac = mac,
            ip = ip,
            hostname = hostName,
            labels = labels,
            blockedAt = blockAt
        )
        DeviceManagerCache.put(newDevice)
        return newDevice
    }

    private fun updateLabels(
        mac: String,
        ip: String,
        statusFlag: String,
        blockedFlag: String
    ): DeviceModel? {
        val existing = DeviceManagerCache.getDevice(mac) ?: return null
        val currentLabels = existing.labels.toMutableSet()
        var changed = false

        val isOnline = statusFlag == "1"
        if (isOnline) {
            if (DeviceLabel.Offline in currentLabels) { currentLabels -= DeviceLabel.Offline; changed = true }
            if (DeviceLabel.Online !in currentLabels) { currentLabels += DeviceLabel.Online; changed = true }
        } else {
            if (DeviceLabel.Online in currentLabels) { currentLabels -= DeviceLabel.Online; changed = true }
            if (DeviceLabel.Offline !in currentLabels) { currentLabels += DeviceLabel.Offline; changed = true }
        }

        val isBlocked = blockedFlag == "1"
        if (isBlocked) {
            if (DeviceLabel.Blocked !in currentLabels) { currentLabels += DeviceLabel.Blocked; changed = true }
        } else {
            if (DeviceLabel.Blocked in currentLabels) { currentLabels -= DeviceLabel.Blocked; changed = true }
        }

        val updated = existing.copy(ip = ip, labels = currentLabels)
        if (changed) DeviceManagerCache.put(updated)
        return updated
    }
}
