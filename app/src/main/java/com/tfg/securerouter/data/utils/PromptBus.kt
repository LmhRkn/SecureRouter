package com.tfg.securerouter.data.utils

import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

object PromptBus {
    data class Request(val spec: AlertSpec, val reply: (Boolean) -> Unit)

    // Hot stream; mantiene 1 en buffer por si el host tarda en aparecer
    val flow = MutableSharedFlow<Request>(extraBufferCapacity = 1)

    suspend fun confirm(spec: AlertSpec): Boolean =
        suspendCancellableCoroutine { cont ->
            flow.tryEmit(Request(spec) { ok ->
                if (cont.isActive) cont.resume(ok)
            })
        }

    suspend fun confirmOrDefault(spec: AlertSpec, default: Boolean, timeoutMs: Long): Boolean =
        withTimeoutOrNull(timeoutMs) { confirm(spec) } ?: default
}
