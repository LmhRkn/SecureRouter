package com.tfg.securerouter.data.automatization.registry

import com.tfg.securerouter.data.automatization.automatizations.after_sh_login.SshPasswdDetector
import com.tfg.securerouter.data.automatization.automatizations.before_ad_blocker.ChangeDnsmasqPort
import com.tfg.securerouter.data.automatization.automatizations.before_opening.ChooseDateTimeZone
import com.tfg.securerouter.data.automatization.automatizations.before_opening.DetectPackageManagerTask
import com.tfg.securerouter.data.automatization.automatizations.before_ad_blocker.InstallAdGuardHome
import com.tfg.securerouter.data.automatization.automatizations.before_opening.InstallCurl
import com.tfg.securerouter.data.automatization.automatizations.on_speedtest.InstallSpeedtestOokla
import com.tfg.securerouter.data.automatization.automatizations.before_opening.InstallVnstat
import com.tfg.securerouter.data.automatization.automatizations.before_opening.PrepareDnsFirewall
import com.tfg.securerouter.data.automatization.automatizations.before_opening.ShPasswdSetter
import com.tfg.securerouter.data.automatization.automatizations.on_vpn.GenerateServerRSA
import com.tfg.securerouter.data.automatization.automatizations.on_vpn.InstallQrencode
import com.tfg.securerouter.data.automatization.automatizations.on_vpn.InstallWireguardTools
import com.tfg.securerouter.data.automatization.automatizations.on_vpn.VPNConfigureFirewall
import com.tfg.securerouter.data.automatization.automatizations.on_vpn.VPNConfigureNetwork
import com.tfg.securerouter.data.automatization.automatizations.on_vpn.VPNGetNumberDevices

object AutomatizationRegistryBeforeOpening {
    val factories: List<TaskFactory> = listOf(
        ::ShPasswdSetter,
        ::DetectPackageManagerTask,
        ::InstallCurl,
        ::InstallVnstat,
        ::ChooseDateTimeZone,
        ::PrepareDnsFirewall,
        // ::EnsureCurlTask, ...
    )
}

object AutomatizationRegistryAfterSHLogin {
    val factories: List<TaskFactory> = listOf(
        ::SshPasswdDetector,
        // ::EnsureCurlTask, ...
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
        ::ChangeDnsmasqPort,
    )
}

object AutomatizationRegistryOnVPN {
    val factories: List<TaskFactory> = listOf(
        ::InstallWireguardTools,
        ::InstallQrencode,
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
