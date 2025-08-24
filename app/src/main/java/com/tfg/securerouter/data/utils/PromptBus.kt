package com.tfg.securerouter.data.utils

import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.notice.model.alerts.TextPromptSpec
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

object PromptBus {
    // -------- Alerts (confirm) --------
    data class Request(val spec: AlertSpec, val reply: (Boolean) -> Unit)
    val flow = MutableSharedFlow<Request>(extraBufferCapacity = 1)

    suspend fun confirm(spec: AlertSpec): Boolean =
        suspendCancellableCoroutine { cont ->
            flow.tryEmit(Request(spec) { ok ->
                if (cont.isActive) cont.resume(ok)
            })
        }

    suspend fun confirmOrDefault(spec: AlertSpec, default: Boolean, timeoutMs: Long): Boolean =
        withTimeoutOrNull(timeoutMs) { confirm(spec) } ?: default

    // -------- Text prompts --------
    data class TextRequest(val spec: TextPromptSpec, val reply: (String?) -> Unit)
    val textFlow = MutableSharedFlow<TextRequest>(extraBufferCapacity = 1)

    suspend fun askText(spec: TextPromptSpec): String? =
        suspendCancellableCoroutine { cont ->
            textFlow.tryEmit(TextRequest(spec) { text ->
                if (cont.isActive) cont.resume(text)
            })
        }

    suspend fun textOrDefault(spec: TextPromptSpec, default: String, timeoutMs: Long): String =
        withTimeoutOrNull(timeoutMs) { askText(spec) } ?: default
}
