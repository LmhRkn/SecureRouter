package com.tfg.securerouter.data.app.automatization.automatizations.on_speedtest

import com.tfg.securerouter.R
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.app.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.app.notice.utils.PromptBus

class InstallSpeedtestOokla(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 180_000L

    private fun lastNonEmpty(out: String) =
        out.lineSequence().map { it.trim() }.lastOrNull { it.isNotEmpty() }.orEmpty()

    private suspend fun shLastLine(cmd: String) =
        lastNonEmpty(sh("( { $cmd ; } 2>/dev/null || true) | tail -n 1"))

    private suspend fun isSpeedtestInstalled(): Int {
        val res = shLastLine("command -v speedtest >/dev/null 2>&1 && echo 1 || echo 0")
        if (AppSession.cancelledSpeedTestByUser) return -1
        return if (res == "1") -1 else 1
    }
    override suspend fun shouldRun(router: RouterInfo?) = isSpeedtestInstalled()

    override suspend fun execute(): Boolean {
        if (isSpeedtestInstalled() == -1) return true

        val eulaAccepted = PromptBus.confirmOrDefault(EulaSpec, default = false, timeoutMs = 60_000)
        if (!eulaAccepted) {
            AppSession.cancelledSpeedTestByUser = true
            return false
        }
        val privacyAccepted = PromptBus.confirmOrDefault(PrivacySpec, default = false, timeoutMs = 60_000)
        if (!privacyAccepted) {
            AppSession.cancelledSpeedTestByUser = true
            return false
        }

        val script = """
            set -eu

            TMPDIR="$(mktemp -d -t spdXXXXXX || echo /tmp/spd.${'$'}${'$'})"
            mkdir -p "${'$'}TMPDIR"
            cd "${'$'}TMPDIR"

            ARCH="$(uname -m || true)"
            PKG=""
            case "${'$'}ARCH" in
              aarch64) PKG="aarch64" ;;
              armv7l|armv6l|armv7|armv6) PKG="armhf" ;;
              x86_64|amd64) PKG="x86_64" ;;
              i386|i686) PKG="i386" ;;
              mips) PKG="mips" ;;
              mipsel) PKG="mipsel" ;;
              *) PKG="" ;;
            esac

            if [ -z "${'$'}PKG" ]; then
              echo "Arquitectura '${'$'}ARCH' no soportada por este instalador."
              exit 0
            fi

            URL="https://install.speedtest.net/app/cli/ookla-speedtest-1.2.0-linux-${'$'}PKG.tgz"
            TARBALL="speedtest.tgz"

            if command -v curl >/dev/null 2>&1; then
              curl -fsSLo "${'$'}TARBALL" "${'$'}URL"
            else
              wget -O "${'$'}TARBALL" "${'$'}URL"
            fi

            tar -xzf "${'$'}TARBALL"
            BIN="$(find . -maxdepth 2 -type f -name speedtest | head -n 1 || true)"
            if [ -z "${'$'}BIN" ]; then
              echo "No se encontró el binario 'speedtest' en el tarball."
              exit 0
            fi

            chmod +x "${'$'}BIN"
            mv -f "${'$'}BIN" /usr/bin/speedtest

            # --- Registrar aceptación para evitar prompts en el router ---
            /usr/bin/speedtest --accept-license --accept-gdpr --version >/dev/null 2>&1 || true

            cd /
            rm -rf "${'$'}TMPDIR"

            if command -v speedtest >/dev/null 2>&1; then
              echo "OK"
            else
              echo "FAIL"
            fi
        """.trimIndent()

        sh("( { $script ; } >/dev/null 2>&1 || true)")

        return isSpeedtestInstalled() == -1
    }

    companion object {
        val EulaSpec = AlertSpec(
            title = R.string.speedtest_eula_alert,
            message = R.string.speedtest_eula_alert_body,
            showCancel = true
        )

        val PrivacySpec = AlertSpec(
            title = R.string.speedtest_gdpr_alert,
            message = R.string.speedtest_gdpr_alert_body,
            showCancel = true
        )
    }
}
