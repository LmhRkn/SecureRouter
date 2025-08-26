package com.tfg.securerouter.data.app.automatization.automatizations.before_opening

import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class DetectPackageManagerTask(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {
    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val saved = router?.installerPackage
        return if (saved.isNullOrBlank()) 1 else -1
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val detectCmd = """
            { opkg --version >/dev/null 2>&1 && echo opkg; } \
            || { apk --version >/dev/null 2>&1 && echo apk; } \
            || { { [ -x /bin/opkg ] || [ -x /usr/bin/opkg ]; } && echo opkg; } \
            || { { [ -x /sbin/apk ] || [ -x /bin/apk ] || [ -x /usr/sbin/apk ] || [ -x /usr/bin/apk ]; } && echo apk; } \
            || echo none
        """.trimIndent()

        val raw = sh(detectCmd)
        val pm = raw
            .lineSequence()
            .map { it.trim() }
            .firstOrNull { it == "opkg" || it == "apk" }
            ?: "none"

        return if (pm == "opkg" || pm == "apk") {
            RouterSelectorCache.update(id) { r -> r.copy(installerPackage = pm) }
            AppSession.packageInstaller = pm
            true
        } else {
            false
        }
    }
}
