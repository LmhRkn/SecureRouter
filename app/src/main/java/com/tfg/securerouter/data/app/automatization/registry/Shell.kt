package com.tfg.securerouter.data.app.automatization.registry

import com.tfg.securerouter.data.app.automatization.AutomatizationDefault

typealias Shell = suspend (String) -> String
typealias TaskFactory = (Shell) -> AutomatizationDefault
