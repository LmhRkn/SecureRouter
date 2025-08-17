// com/tfg/securerouter/data/automatization/registry/Types.kt
package com.tfg.securerouter.data.automatization.registry

import com.tfg.securerouter.data.automatization.AutomatizationDefault

typealias Shell = suspend (String) -> String
typealias TaskFactory = (Shell) -> AutomatizationDefault
