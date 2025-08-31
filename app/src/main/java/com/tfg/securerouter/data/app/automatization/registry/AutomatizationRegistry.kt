package com.tfg.securerouter.data.app.automatization.registry

import com.tfg.securerouter.data.app.automatization.automatizations.after_ssh_login.SshPasswdDetector
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.ChooseDateTimeZone
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.DetectPackageManagerTask
import com.tfg.securerouter.data.app.automatization.automatizations.before_ad_blocker.InstallAdGuardHome
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.InstallCurl
import com.tfg.securerouter.data.app.automatization.automatizations.on_speedtest.InstallSpeedtestOokla
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.InstallVnstat
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.IsActualizationExists
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.IsActualizationFileCreated
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.PrepareDnsFirewall
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.RouterHasDomain
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.ShPasswdSetter
import com.tfg.securerouter.data.app.automatization.automatizations.on_vpn.GenerateServerRSA
import com.tfg.securerouter.data.app.automatization.automatizations.before_opening.InstallQrencode
import com.tfg.securerouter.data.app.automatization.automatizations.on_vpn.InstallWireguardTools
import com.tfg.securerouter.data.app.automatization.automatizations.on_vpn.VPNConfigureFirewall
import com.tfg.securerouter.data.app.automatization.automatizations.on_vpn.VPNConfigureNetwork
import com.tfg.securerouter.data.app.automatization.automatizations.on_vpn.VPNGetNumberDevices

object AutomatizationRegistryBeforeOpening {
    val factories: List<TaskFactory> = listOf(
        ::ShPasswdSetter,
        ::RouterHasDomain,
        ::DetectPackageManagerTask,
        ::InstallCurl,
        ::InstallVnstat,
        ::ChooseDateTimeZone,
        ::PrepareDnsFirewall,
        ::IsActualizationFileCreated,
        ::InstallQrencode,
        ::IsActualizationExists,
    )
}

object AutomatizationRegistryAfterSHLogin {
    val factories: List<TaskFactory> = listOf(
        ::SshPasswdDetector,
    )
}

object AutomatizationRegistryOnSpeedtest {
    val factories: List<TaskFactory> = listOf(
        ::InstallSpeedtestOokla,
    )
}

object AutomatizationRegistryBeforeAdBlocker {
    val factories: List<TaskFactory> = listOf(
        ::InstallAdGuardHome,
    )
}

object AutomatizationRegistryOnVPN {
    val factories: List<TaskFactory> = listOf(
        ::InstallWireguardTools,
        ::GenerateServerRSA,
        ::VPNConfigureFirewall,
        ::VPNConfigureNetwork,
        ::VPNGetNumberDevices,
    )
}

object AutomatizationBuilder {
    fun createAll(factories: List<TaskFactory>, sh: Shell) =
        factories.map { it(sh) }
}
