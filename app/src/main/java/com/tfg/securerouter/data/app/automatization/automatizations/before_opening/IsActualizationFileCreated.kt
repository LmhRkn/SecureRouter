package com.tfg.securerouter.data.app.automatization.automatizations.before_opening

import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault

class IsActualizationFileCreated(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {
    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val exists = sh("[ -f /root/check_update.sh ] && echo EXIST || echo MISSING")
            .trim()

        return if (exists.contains("EXIST")) -1 else 1
    }

    override suspend fun execute(): Boolean {
        val ensureScriptCmd = """
            set -e
            cat > /root/check_update.sh << 'EOF'
            #!/bin/sh

            # Detectar target (ej: ath79/generic)
            target=$(grep DISTRIB_TARGET= /etc/openwrt_release | cut -d"'" -f2)

            # Detectar nombre de la placa (ej: tplink,archer-c60-v3)
            board=$(cat /tmp/sysinfo/board_name)

            # Última versión estable disponible en el repo
            latest=$(curl -fsS https://downloads.openwrt.org/releases/ \
                | grep -Eo 'href="[0-9]+\.[0-9]+\.[0-9]+/"' \
                | cut -d'"' -f2 | tr -d '/' \
                | sort -V | tail -1)

            # Buscar imagen sysupgrade en la carpeta del target
            url="https://downloads.openwrt.org/releases/${'$'}latest/targets/${'$'}target/"
            image=$(curl -fsS "${'$'}url" | grep "sysupgrade.bin" | grep "${'$'}board" | cut -d'"' -f2)

            if [ -n "${'$'}image" ]; then
                echo "URL Found"
                echo "${'$'}url${'$'}image"
                echo
                echo "command"
                echo "cd /tmp"
                echo "wget ${'$'}url${'$'}image"
                echo "sysupgrade -n /tmp/$(basename ${'$'}image)"
                echo "end_command"
            else
                echo "\nNOT_FOUND\n"
            fi
            EOF
            chmod +x /root/check_update.sh
        """.trimIndent()

        sh(ensureScriptCmd)

        return true
    }
}

