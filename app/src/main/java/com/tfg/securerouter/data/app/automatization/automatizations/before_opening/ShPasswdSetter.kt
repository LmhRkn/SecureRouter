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
        Log.d("ShPasswdSetter", "Router: $router")
        if (router == null) return false

        val pwd = decryptPassword(router.sshPassword ?: "")

        val res = sh(
            """
        # Detectar daemon y política de password
        if command -v uci >/dev/null 2>&1 && uci -q show dropbear >/dev/null 2>&1; then
          daemon=dropbear
          pw_on=$(
            uci -q show dropbear \
            | sed -n 's/^dropbear\.@dropbear\[[0-9]\+\]\.PasswordAuth=\(.*\)$/\1/p' \
            | tr 'A-Z' 'a-z' \
            | grep -q '^on' && echo on || echo off
          )
        elif [ -f /etc/ssh/sshd_config ]; then
          daemon=sshd
          val=${'$'}(awk 'tolower('${'$'}1')=="passwordauthentication"{print tolower('${'$'}2')}' /etc/ssh/sshd_config | tail -n1)
          [ -z "${'$'}val" ] && val=unknown
          pw_on=${'$'}val
        else
          daemon=unknown
          pw_on=unknown
        fi

        # Estado de la contraseña de root
        root_hash=${'$'}(awk -F: '${'$'}1=="root"{print ${'$'}2}' /etc/shadow 2>/dev/null)

        echo "SSH_DAEMON:${'$'}daemon"
        echo "SSH_PASSWORD_AUTH:${'$'}pw_on"

        needs_set=0
        case "${'$'}root_hash" in
          ""|"!"|"*"|"!!") needs_set=1 ;;
        esac

        if [ "${'$'}needs_set" -eq 1 ]; then
          echo "ROOT_PASS:empty"
          pwd='${pwd}'

          # Intento 1: passwd no interactivo
          if printf "%s\n%s\n" "${'$'}pwd" "${'$'}pwd" | passwd root >/dev/null 2>&1; then
            echo "NEW_PASSWORD:${'$'}pwd"
            echo OK
          else
            echo "ERROR:failed to set password"
          fi
        else
          echo "ROOT_PASS:set_or_locked_with_hash"
          echo OK
        fi
        """.trimIndent()
        ).trim()

        Log.d("ShPasswdSetter", "Pwd: $pwd")
        Log.d("ShPasswdSetter", "Sh: $sh")


        val setOk = res.lineSequence().any { it.startsWith("NEW_PASSWORD:") }
        Log.d("ShPasswdSetter", "setOk: $setOk")
        return res.lineSequence().lastOrNull() == "OK" && setOk
    }
}
