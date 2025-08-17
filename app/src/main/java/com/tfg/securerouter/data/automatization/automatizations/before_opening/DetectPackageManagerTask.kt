package com.tfg.securerouter.data.automatization.automatizations.before_opening

import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelctorCache
import com.tfg.securerouter.data.utils.AppSession

class DetectPackageManagerTask(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override suspend fun shouldRun(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false
        val saved = RouterSelctorCache.getRouter(id)?.installerPackage
        return saved.isNullOrBlank()
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val pm = sh(
            "if command -v opkg >/dev/null 2>&1; then echo opkg; " +
                    "elif command -v apk >/dev/null 2>&1; then echo apk; " +
                    "else echo none; fi"
        ).trim()

        return if (pm == "opkg" || pm == "apk") {
            RouterSelctorCache.update(id) { r -> r.copy(installerPackage = pm) }
            AppSession.packageInstaller = pm
            true
        } else {
            false
        }
    }
}