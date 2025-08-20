package com.tfg.securerouter.data.automatization

import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo

abstract class AutomatizationDefault {

    abstract val timeoutMs: Long

    suspend fun runIfNeeded(router: RouterInfo? = null): Boolean {
        return if (shouldRun(router) == 1) execute() else if (shouldRun(router) == -1) true else false
    }

    protected abstract suspend fun shouldRun(router: RouterInfo? = null): Int
    protected abstract suspend fun execute(): Boolean

}
