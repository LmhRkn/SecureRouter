package com.tfg.securerouter.data.app.screens.wifi.model.load

import com.tfg.securerouter.data.app.screens.ScreenComponentModelDefault
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class WifiQrState(
    val ifaceIndex: Int = 0,
    val ssid: String = "",
    val auth: String = "",
    val hidden: Boolean = false,
    val txt: String = "",
    val qrAnsi: String = "",
    val pngPath: String? = null,
    val error: String? = null
)

/**
 * Modelo que genera el QR de la Wi-Fi desde UCI (OpenWrt).
 */
class WifiGetWifiQRModel(
    private val sharedCache: MutableMap<String, Any>,
    private val ifaceIndex: Int = 0,
) : ScreenComponentModelDefault {

    private val _state = MutableStateFlow(WifiQrState(ifaceIndex = ifaceIndex))
    val state: StateFlow<WifiQrState> = _state

    override suspend fun loadData(): Boolean {
        val cacheKey = "wifi_qr_iface_$ifaceIndex"

        val D = '$'
        val cmd = buildString {
            appendLine(
                """
                IFACE=$ifaceIndex
    
                SSID=${D}(uci -q get wireless.@wifi-iface[${D}IFACE].ssid)
                ENCR=${D}(uci -q get wireless.@wifi-iface[${D}IFACE].encryption)
                PASS=${D}(uci -q get wireless.@wifi-iface[${D}IFACE].key)
                HIDDEN=${D}(uci -q get wireless.@wifi-iface[${D}].hidden 2>/dev/null || echo 0)
    
                case "${D}ENCR" in
                  none|"") AUTH="nopass" ;;
                  wep*)    AUTH="WEP"    ;;
                  *)       AUTH="WPA"    ;;  # vale para WPA/WPA2/WPA3
                esac
    
    
                esc(){ echo -n "${D}1" | sed -e 's/\\/\\\\/g' -e 's/;/\\;/g' -e 's/,/\\,/g' -e 's/:/\\:/g'; }
    
                TXT="WIFI:S:${D}(esc "${D}SSID");T:${D}AUTH"
                [ "${D}AUTH" != "nopass" ] && TXT="${D}TXT;P:${D}(esc "${D}PASS")"
                [ "${D}HIDDEN" = "1" ] && TXT="${D}TXT;H:true"
                TXT="${D}TXT;;"
    
                # Metadatos para el parseo
                printf "__QR_META_START__%s|%s|%s|%s__QR_META_END__" "${D}SSID" "${D}AUTH" "${D}HIDDEN" "${D}TXT"
    
                # QR ANSI para mostrar
                printf "__QR_START__"
                echo "${D}TXT" | qrencode -t ANSIUTF8
                printf "__QR_END__"
            """.trimIndent()
            )
        }

        return safeLoad(
            cache = sharedCache,
            command = cmd,
            cacheKey = cacheKey,
            parse = { output -> parseWifiQrOutput(output, ifaceIndex) },
            setState = { st -> _state.value = st }
        )
    }


    private fun parseWifiQrOutput(raw: String, iface: Int): WifiQrState {
        val meta = raw.substringAfter("__QR_META_START__", "")
            .substringBefore("__QR_META_END__", "")
        val parts = meta.split("|", limit = 4)
        val ssid = parts.getOrNull(0).orEmpty()
        val auth = parts.getOrNull(1).orEmpty()
        val hiddenStr = parts.getOrNull(2).orEmpty()
        val txt = parts.getOrNull(3).orEmpty()
        val hidden = hiddenStr == "1" || hiddenStr.equals("true", ignoreCase = true)

        val qrAnsi = raw.substringAfter("__QR_START__", "")
            .substringBefore("__QR_END__", "")

        val pngPath = raw.substringAfter("__PNG_START__", "")
            .substringBefore("__PNG_END__", "")
            .ifBlank { null }

        if (qrAnsi.isBlank()) {
            return WifiQrState(
                ifaceIndex = iface,
                error = "No se pudo generar el QR (salida vacía)."
            )
        }

        return WifiQrState(
            ifaceIndex = iface,
            ssid = ssid,
            auth = auth,
            hidden = hidden,
            txt = txt,
            qrAnsi = qrAnsi,
            pngPath = pngPath
        )
    }
}
