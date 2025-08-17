package com.tfg.securerouter.data.automatization.automatizations.before_opening

import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelctorCache
import com.tfg.securerouter.data.utils.AppSession

class InstallCurl(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    private suspend fun isCurlInstalled(): Boolean =
        sh("hash -r; command -v curl >/dev/null 2>&1 && echo 1 || echo 0").trim() == "1"

    override suspend fun shouldRun(): Boolean {
        return !isCurlInstalled()
    }

    override suspend fun execute(): Boolean {
        val id = AppSession.routerId?.toString() ?: return false

        val pm: String? = AppSession.packageInstaller
            ?: RouterSelctorCache.getRouter(id)?.installerPackage

        when (pm) {
            "opkg" -> {
                sh("opkg update >/dev/null 2>&1 || true; opkg install curl >/dev/null 2>&1 || true")
            }
            "apk" -> {
                sh("apk add --no-cache curl >/dev/null 2>&1 || true")
            }
            else -> return false
        }

        return isCurlInstalled()
    }
}