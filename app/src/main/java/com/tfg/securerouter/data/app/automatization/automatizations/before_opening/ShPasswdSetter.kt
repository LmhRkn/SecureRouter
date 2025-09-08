package com.tfg.securerouter.data.app.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.decryptPassword

class ShPasswdSetter(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        return if (AppSession.createSSHPassword == null) -1 else 1
    }

    override suspend fun execute(): Boolean {
        val router: RouterInfo? = RouterSelectorCache.getRouter(AppSession.routerId.toString())
        if (router == null) return false

        val pwd = decryptPassword(router.sshPassword ?: "")

        val res = sh(
            """
        #!/bin/sh
        pwd="${'$'}1"
        [ -n "$pwd" ] || { echo "ERROR"; exit 1; }
        
        if command -v chpasswd >/dev/null 2>&1; then
          printf 'root:%s\n' "$pwd" | chpasswd >/dev/null 2>&1 || { echo "ERROR"; exit 2; }
        else
          printf '%s\n%s\n' "$pwd" "$pwd" | passwd root >/dev/null 2>&1 || { echo "ERROR"; exit 3; }
        fi
        
        echo "OK"

        """.trimIndent()
        ).trim()


        val setOk = res.lineSequence().any { it.startsWith("NEW_PASSWORD:") }
        return res.lineSequence().lastOrNull() == "OK" && setOk
    }
}
