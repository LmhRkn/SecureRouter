package com.tfg.securerouter.data.automatization

abstract class AutomatizationDefault {

    suspend fun runIfNeeded(): Boolean {
        return if (shouldRun()) execute() else false
    }

    protected abstract suspend fun shouldRun(): Boolean

    protected abstract suspend fun execute(): Boolean
}
