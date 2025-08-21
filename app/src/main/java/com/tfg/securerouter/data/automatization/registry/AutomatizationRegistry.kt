// com/tfg/securerouter/data/automatization/registry/AutomatizationRegistries.kt
package com.tfg.securerouter.data.automatization.registry

import com.tfg.securerouter.data.automatization.automatizations.before_opening.ChooseDateTimeZone
import com.tfg.securerouter.data.automatization.automatizations.before_opening.DetectPackageManagerTask
import com.tfg.securerouter.data.automatization.automatizations.before_opening.InstallCurl
import com.tfg.securerouter.data.automatization.automatizations.on_speedtest.InstallSpeedtestOokla
import com.tfg.securerouter.data.automatization.automatizations.before_opening.InstallVnstat
import com.tfg.securerouter.data.automatization.automatizations.before_opening.PrepareDnsFirewall
import com.tfg.securerouter.data.automatization.automatizations.before_opening.SshPasswdDetector

object AutomatizationRegistryBeforeOpening {
    val factories: List<TaskFactory> = listOf(
        ::SshPasswdDetector,
        ::DetectPackageManagerTask,
        ::InstallCurl,
        ::InstallVnstat,
        ::ChooseDateTimeZone,
        ::PrepareDnsFirewall,
        // ::EnsureCurlTask, ...
    )
}

object AutomatizationRegistryOnSpeedtest {
    val factories: List<TaskFactory> = listOf(
        ::InstallSpeedtestOokla,
    )
}

object AutomatizationBuilder {
    fun createAll(factories: List<TaskFactory>, sh: Shell) =
        factories.map { it(sh) }
}
