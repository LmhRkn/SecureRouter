package com.tfg.securerouter.data.app.automatization.automatizations.on_vpn

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession

class GenerateServerRSA(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    // Rutas esperadas
    private val baseDir = "vpn"
    private val srvDir  = "$baseDir/router"
    private val privKey = "$srvDir/wgserver.key"
    private val pubKey  = "$srvDir/wgserver.pub"

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val saved = router?.installerPackage
        if (saved.isNullOrBlank()) return 1

        val check = sh(
            """
            [ -s "$privKey" ] && [ -s "$pubKey" ] && echo KEYS_OK || echo KEYS_FAIL
            """.trimIndent()
        ).trim()

        val hasOk = check
            .lineSequence()
            .map { it.trim() }
            .any { it == "KEYS_OK" }

        Log.d("GenerateServerRSA", "check output: $check, hasOk=$hasOk")

        return if (hasOk) -1 else 1
    }

    override suspend fun execute(): Boolean {
        AppSession.firstVPN = true
        val id = AppSession.routerId?.toString() ?: return false

        val pm: String? = AppSession.packageInstaller
            ?: RouterSelectorCache.getRouter(id)?.installerPackage

        val BASE = "vpn"
        val DIR = "$BASE/router"
        val DEV = "$BASE/devices"
        val PRIV = "$DIR/wgserver.key"
        val PUB = "$DIR/wgserver.pub"

        val keyPrepScript = """
            mkdir -p "$DIR"
            mkdir -p "$DEV"

            umask go=
            wg genkey | tee $PRIV | wg pubkey > $PUB


            [ -s "$PRIV" ] && [ -s "$PUB" ] && echo "OK" || (echo "FAIL" && exit 1)

        """.trimIndent()

        val result = sh(keyPrepScript)
        val ok = result.lineSequence().any { it.trim() == "OK" }
        return ok
    }
}
