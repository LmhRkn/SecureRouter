// com/tfg/securerouter/data/automatization/ExecuteAutomatizations.kt
package com.tfg.securerouter.data.automatization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.tfg.securerouter.data.automatization.registry.Shell
import com.tfg.securerouter.data.automatization.registry.TaskFactory
import com.tfg.securerouter.data.automatization.registry.AutomatizationBuilder

@Composable
fun ExecuteAutomatizations(
    factories: List<TaskFactory>,
    sh: Shell,
    key: Any? = Unit
) {
    LaunchedEffect(key) {
        val tasks = AutomatizationBuilder.createAll(factories, sh)
        tasks.forEach { it.runIfNeeded() }
    }
}

suspend fun ExecuteAutomatizationsBlocking(
    factories: List<TaskFactory>,
    sh: Shell
) {
    val tasks = AutomatizationBuilder.createAll(factories, sh)
    tasks.forEach { it.runIfNeeded() }
}
