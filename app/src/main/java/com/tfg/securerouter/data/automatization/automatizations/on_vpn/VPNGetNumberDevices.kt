package com.tfg.securerouter.data.automatization.automatizations.on_vpn

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.utils.AppSession

class VPNGetNumberDevices(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int = 1

    override suspend fun execute(): Boolean {
        val BASE = "/root/vpn"
        val DEV = "$BASE/devices"

        val script = """
            if [ ! -d "$DEV" ]; then
              echo "COUNT=0"
              echo "FREE=2"
              exit 0
            fi

            # Contar peers en 2..254
            count=0
            i=2
            while [ "${'$'}i" -le 254 ]; do
              if [ -d "$DEV/peer_${'$'}i" ]; then
                count=$((count+1))
              fi
              i=$((i+1))
            done

            # Buscar el primer hueco libre en 2..254
            free=""
            i=2
            while [ "${'$'}i" -le 254 ]; do
              if [ ! -d "$DEV/peer_${'$'}i" ]; then
                free="${'$'}i"
                break
              fi
              i=$((i+1))
            done

            echo "COUNT=${'$'}count"
            echo "FREE=${'$'}free"
        """.trimIndent()

        val out = sh(script).trim()
        Log.d("VPNConfigureFirewallLog", "shell out:\n$out")

        val count = Regex("^COUNT=(\\d+)", RegexOption.MULTILINE)
            .find(out)?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 0

        val firstFree = Regex("^FREE=(\\d+)", RegexOption.MULTILINE)
            .find(out)?.groupValues?.getOrNull(1)?.toIntOrNull()

        AppSession.newDeviceVPN = (count == 0)
        AppSession.nextDeviceVPN = firstFree ?: 2

        Log.d(
            "VPNConfigureFirewallLog",
            "devices count=$count, first free=${firstFree ?: "none"}; " +
                    "newDeviceVPN=${AppSession.newDeviceVPN}, nextDeviceVPN=${AppSession.nextDeviceVPN}"
        )

        return true
    }
}
