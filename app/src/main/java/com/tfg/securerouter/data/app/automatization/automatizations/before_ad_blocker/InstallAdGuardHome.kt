package com.tfg.securerouter.data.app.automatization.automatizations.before_ad_blocker

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class InstallAdGuardHome(
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

    private suspend fun isAdGuardHomeInstalled(): Int {
        val res = shLastLine("command -v AdGuardHome >/dev/null 2>&1 && echo 1 || echo 0")
        Log.d("InstallAdGuardHome", "isAdGuardHomeInstalled(last)='$res'")
        return if (res == "1") -1 else 1
    }

    override suspend fun shouldRun(router: RouterInfo?): Int {
        return isAdGuardHomeInstalled()
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val pm: String? = AppSession.packageInstaller
            ?: RouterSelectorCache.getRouter(id)?.installerPackage

        when (pm) {
            "opkg" -> {
                sh("(opkg update >/dev/null 2>&1 || true; opkg install adguardhome >/dev/null 2>&1 || true) >/dev/null 2>&1 || true")
            }
            "apk" -> {
                sh("(apk add --no-cache adguardhome >/dev/null 2>&1 || true) >/dev/null 2>&1 || true")
            }
            else -> return false
        }

        return isAdGuardHomeInstalled() == -1
    }
}
