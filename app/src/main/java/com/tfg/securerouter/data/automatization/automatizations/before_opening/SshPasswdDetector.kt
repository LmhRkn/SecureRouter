// com/tfg/securerouter/data/automatization/automatizations/before_opening/SshPasswdDetector.kt
package com.tfg.securerouter.data.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.encryptPassword

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
        return if (state) 1 else 0
    }

    override suspend fun execute(): Boolean {
        Log.d("aaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaa")
//        val pwd = PasswordGenerator.generar(
//            longitud = 24,
//            minus = true, mayus = true, digitos = true, simbolos = true,
//            evitarSimilares = true, exigirCadaClase = true
//        )
        val pwd = "12345678"

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

        val setOk = res.lineSequence().any { it.startsWith("NEW_PASSWORD:") }
        if (setOk) {
            RouterSelectorCache.update(AppSession.routerId.toString()) { r ->
                r.copy(sshPassword = encryptPassword(pwd))
            }
        }
        return res.lineSequence().lastOrNull() == "OK" && setOk
    }
}
