// com/tfg/securerouter/data/automatization/ExecuteAutomatizations.kt
package com.tfg.securerouter.data.app.automatization

import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.app.automatization.registry.AutomatizationBuilder
import com.tfg.securerouter.data.app.automatization.registry.Shell
import com.tfg.securerouter.data.app.automatization.registry.TaskFactory
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "ExecuteAutomatizations"

private const val DEFAULT_TIMEOUT = 30_000L
private const val MAX_ATTEMPTS = 3
private const val RETRY_DELAY_BASE_MS = 750L

/**
 * Versión suspend que ejecuta las automatizaciones con logs y reintentos.
 */
suspend fun executeAutomationsBlocking(
    router: RouterInfo?,
    factories: List<TaskFactory>,
    sh: Shell
) {
    val tasks = AutomatizationBuilder.createAll(factories, sh)
    Log.d(TAG, "tasks: $tasks")

    for ((i, task) in tasks.withIndex()) {
        val name = task::class.simpleName ?: task.toString()
        val timeout = (task as? AutomatizationDefault)?.timeoutMs ?: DEFAULT_TIMEOUT

        var attempt = 1
        while (attempt <= MAX_ATTEMPTS) {
            val t0 = SystemClock.elapsedRealtime()
            val tag = "→ [${i + 1}/${tasks.size}] $name (try $attempt/$MAX_ATTEMPTS, timeout=${timeout}ms)"
            Log.d(TAG, "$tag: start")
            try {
                val router2 = RouterSelectorCache.getRouter(router!!.id.toString())
                val ran: Boolean? = withTimeoutOrNull(timeout) { task.runIfNeeded(router2) }
                val dt = SystemClock.elapsedRealtime() - t0
                when (ran) {
                    null  -> Log.w(TAG, "$tag: TIMEOUT tras ${dt}ms")
                    true  -> {
                        Log.d(TAG, "$tag: done (executed=true, ${dt}ms)")
                        break
                    }
                    false -> Log.d(TAG, "$tag: done (executed=false, ${dt}ms)")
                }
                if (ran == true || attempt == MAX_ATTEMPTS) break
            } catch (e: TimeoutCancellationException) {
                val dt = SystemClock.elapsedRealtime() - t0
                Log.w(TAG, "$tag: TIMEOUT (throw) tras ${dt}ms")
                if (attempt == MAX_ATTEMPTS) break
            } catch (e: CancellationException) {
                Log.w(TAG, "$tag: CANCELLED externamente")
                throw e
            } catch (t: Throwable) {
                val dt = SystemClock.elapsedRealtime() - t0
                Log.e(TAG, "$tag: ERROR tras ${dt}ms", t)
                if (attempt == MAX_ATTEMPTS) break
            }
            delay(RETRY_DELAY_BASE_MS * attempt)
            attempt++
        }
    }
}

/**
 * Wrapper UI para la versión *blocking*: muestra spinner mientras corre y
 * renderiza [content] al terminar.
 */
@Composable
fun ExecuteAutomationsBlockingUI(
    router: RouterInfo?,
    factories: List<TaskFactory>,
    sh: Shell,
    loadingContent: @Composable () -> Unit = {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    },
    content: @Composable () -> Unit = {}
) {
    var isRunning by remember(router?.id) { mutableStateOf(true) }

    LaunchedEffect(router?.id) {
        try {
            executeAutomationsBlocking(router, factories, sh)
        } finally {
            isRunning = false
        }
    }

    if (isRunning) loadingContent() else content()
}

