package com.tfg.securerouter.data.app.automatization.automatizations.before_opening

import android.util.Log
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.AutomatizationDefault
import com.tfg.securerouter.data.app.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.app.notice.utils.PromptBus

class IsActualizationExists(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {
    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        val output = sh("sh /root/check_update.sh")
            .trim()

        Log.d("IsActualizationExists", "output: $output \noutput.contains(\"NOT_FOUND\"): ${output.contains("NOT_FOUND")}")

        return if (output.contains("El sistema ya esta actualizado")) -1 else 1
    }

    override suspend fun execute(): Boolean {
        val update = PromptBus.confirmOrDefault(UpdateAlert, default = false, timeoutMs = 60_000)
        return true
    }

    companion object {
        val UpdateAlert = AlertSpec(
            title = "Actualización disponible",
            message = """
                Tienes que actualizar el router, 
                no hacerlo supone un riesgo considerable
            """.trimIndent(),
            confirmText = "Aceptar",
            cancelText = "Cancelar",
            showCancel = true
        )
    }
}
