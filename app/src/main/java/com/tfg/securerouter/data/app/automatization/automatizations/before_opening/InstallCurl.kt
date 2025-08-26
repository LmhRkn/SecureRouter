package com.tfg.securerouter.data.app.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class InstallCurl(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override val timeoutMs: Long = 100_000L

    // --- Helpers para eliminar el banner y quedarnos con la última línea útil ---
    private fun lastNonEmpty(out: String): String =
        out.lineSequence()
            .map { it.trim() }
            .lastOrNull { it.isNotEmpty() }
            .orEmpty()

    private suspend fun shLastLine(cmd: String): String =
        lastNonEmpty(sh("( { $cmd ; } 2>/dev/null || true) | tail -n 1"))

    /**
     * Devuelve -1 si curl está instalado (no hay que correr la automatización),
     *  1  si NO está instalado (sí hay que correrla).
     */
    private suspend fun isCurlInstalled(): Int {
        val res = shLastLine("command -v curl >/dev/null 2>&1 && echo 1 || echo 0")
        Log.d("InstallCurl", "isCurlInstalled(last)='$res'")
        return if (res == "1") -1 else 1
    }

    override suspend fun shouldRun(router: RouterInfo?): Int {
        return isCurlInstalled()
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val pm: String? = AppSession.packageInstaller
            ?: RouterSelectorCache.getRouter(id)?.installerPackage

        when (pm) {
            "opkg" -> {
                // Silenciamos stdout/stderr para no traer el banner en la respuesta
                sh("(opkg update >/dev/null 2>&1 || true; opkg install curl >/dev/null 2>&1 || true) >/dev/null 2>&1 || true")
            }
            "apk" -> {
                sh("(apk add --no-cache curl >/dev/null 2>&1 || true) >/dev/null 2>&1 || true")
            }
            else -> return false
        }

        return isCurlInstalled() == -1
    }
}
