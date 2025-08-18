package com.tfg.securerouter.data.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.PasswordGenerator
import com.tfg.securerouter.data.utils.encryptPassword

class SshPasswdDetector(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override suspend fun shouldRun(): Boolean {
        val out = sh(
            """
            root_hash=${'$'}(awk -F: '${'$'}1=="root"{print ${'$'}2}' /etc/shadow 2>/dev/null)
            if [ -z "${'$'}root_hash" ]; then
              echo EMPTY
            else
              echo SET
            fi
            """.trimIndent()
        ).trim()
        Log.d("AAAAAAAAAAAAAAAAAAAA", out)
        return out.lines().lastOrNull() == "EMPTY"
    }

    override suspend fun execute(): Boolean {
//        val nueva = PasswordGenerator.generar(24)
        val nueva =  "123456789"

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
              val=${'$'}(awk 'tolower($1)=="passwordauthentication"{print tolower($2)}' /etc/ssh/sshd_config | tail -n1)
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

            if [ -z "${'$'}root_hash" ]; then
              echo "ROOT_PASS:empty"
              pwd="$nueva"
              # Establecer la contraseña de root de forma no interactiva
              if printf "%s\n%s\n" "${'$'}pwd" "${'$'}pwd" | passwd root >/dev/null 2>&1; then
                echo "NEW_PASSWORD:${'$'}pwd"
                echo OK
              else
                echo "ERROR:failed to set password"
              fi
            else
              echo "ROOT_PASS:set"
              echo OK
            fi
            """.trimIndent()
        ).trim()

        RouterSelectorCache.update(AppSession.routerIp.toString()) { r ->
            r.copy(sshPassword = encryptPassword(nueva))
        }


        return res.lines().lastOrNull() == "OK"
    }
}
