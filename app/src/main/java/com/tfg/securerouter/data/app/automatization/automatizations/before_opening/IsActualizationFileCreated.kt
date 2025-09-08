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
            cat > /root/check_update.sh << 'EOF'
            current_version=${'$'}(cat /etc/openwrt_release | grep DISTRIB_RELEASE | cut -d"'" -f2)
            latest_version=${'$'}(curl -s https://downloads.openwrt.org/releases/ | grep -Eo 'href="[0-9]+\.[0-9]+\.[0-9]+/"' | sort -V | tail -1 | cut -d'"' -f2 | sed 's/\///')

            if [ -z "${'$'}latest_version" ]; then
                echo "Error: No se pudo obtener la ultima version"
                exit 1
            fi

            if [ "${'$'}current_version" = "${'$'}latest_version" ]; then
                echo "El sistema ya esta actualizado"
                exit 0
            fi

            echo "Actualizacion disponible"
            EOF
            chmod +x /root/check_update.sh
        """.trimIndent()

        sh(ensureScriptCmd)

        return true
    }
}

