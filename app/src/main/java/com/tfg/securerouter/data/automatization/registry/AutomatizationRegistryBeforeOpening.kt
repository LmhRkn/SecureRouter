// com/tfg/securerouter/data/automatization/registry/AutomatizationRegistries.kt
package com.tfg.securerouter.data.automatization.registry

import com.tfg.securerouter.data.automatization.automatizations.before_opening.ChooseDateTimeZone
import com.tfg.securerouter.data.automatization.automatizations.before_opening.DetectPackageManagerTask
import com.tfg.securerouter.data.automatization.automatizations.before_opening.InstallCurl
import com.tfg.securerouter.data.automatization.automatizations.before_opening.SshPasswdDetector

object AutomatizationRegistryBeforeOpening {
    val factories: List<TaskFactory> = listOf(
        ::DetectPackageManagerTask,
        ::InstallCurl,
        ::ChooseDateTimeZone,
        ::SshPasswdDetector,
        // ::EnsureCurlTask, ...
    )
}

object AutomatizationRegistryOnConnected {
    val factories: List<TaskFactory> = listOf(
        // ::EnsureTimezoneTask, ...
    )
}

object AutomatizationBuilder {
    fun createAll(factories: List<TaskFactory>, sh: Shell) =
        factories.map { it(sh) }
}
