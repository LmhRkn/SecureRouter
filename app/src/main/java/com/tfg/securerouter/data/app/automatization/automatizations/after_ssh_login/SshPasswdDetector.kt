package com.tfg.securerouter.data.app.automatization.automatizations.after_ssh_login

import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.utils.AppSession

class SshPasswdDetector(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        if (router?.sshPassword != null) return -1

        val out = sh(
            """
                h=$(awk -F: '$1=="root"{print $2}' /etc/shadow 2>/dev/null)
                case "${'$'}h" in
                  ""|"!"|"*"|"!!") echo EMPTY ;;     # sin contraseña o bloqueada
                  "!"*)            echo LOCKED ;;    # bloqueada con hash previo
                  \$*)             echo SET ;;       # hay hash -> hay contraseña
                  *)               echo UNKNOWN ;;
                esac
            """.trimIndent()
        ).trim()

        val state = out.lines().lastOrNull() == "EMPTY"
        if (state) AppSession.createSSHPassword = true else AppSession.createSSHPassword = false

        return -1
    }

    override suspend fun execute(): Boolean { return true }
}
