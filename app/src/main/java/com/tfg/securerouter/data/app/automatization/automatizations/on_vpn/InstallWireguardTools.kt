package com.tfg.securerouter.data.app.automatization.automatizations.on_vpn

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class InstallWireguardTools(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override val timeoutMs: Long = 100_000L

    private fun lastNonEmpty(out: String): String =
        out.lineSequence()
            .map { it.trim() }
            .lastOrNull { it.isNotEmpty() }
            .orEmpty()

    private suspend fun shLastLine(cmd: String): String =
        lastNonEmpty(sh("( { $cmd ; } 2>/dev/null || true) | tail -n 1"))

    private suspend fun isWireguardToolsInstalled(): Int {
        val res = shLastLine("command -v wg >/dev/null 2>&1 && echo 1 || echo 0")
        Log.d("InstallWireguardTools", "isWireguardToolsInstalled(last)='$res'")
        return if (res == "1") -1 else 1
    }

    override suspend fun shouldRun(router: RouterInfo?): Int {
        return isWireguardToolsInstalled()
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val pm: String? = AppSession.packageInstaller
            ?: RouterSelectorCache.getRouter(id)?.installerPackage

        val installCmd = when (pm) {
            "opkg" -> """
        opkg update
        opkg install wireguard-tools || true
        command -v wg >/dev/null 2>&1 && echo "WG_OK" || echo "WG_FAIL"
    """.trimIndent()
            "apk" -> """
        apk update
        apk add --no-cache wireguard-tools || true
        command -v wg >/dev/null 2>&1 && echo "WG_OK" || echo "WG_FAIL"
    """.trimIndent()
            else -> "echo WG_FAIL"
        }

        val installOut = sh(installCmd)
        if (!installOut.lineSequence().any { it.trim() == "WG_FAIL" }) {
            return false
        }

        return isWireguardToolsInstalled() == -1
    }
}
