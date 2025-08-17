package com.tfg.securerouter.data.automatization.automatizations.before_opening

import com.tfg.securerouter.data.automatization.AutomatizationDefault

class AutoCanalChooser(
    private val sh: suspend (String) -> String
) : AutomatizationDefault() {

    override suspend fun shouldRun(): Boolean {
        val out = sh(
            """
        for d in ${'$'}(uci -q show wireless | sed -n 's/^wireless\.\([^=]*\)=wifi-device/\1/p'); do
          v=${'$'}(uci -q get wireless.${'$'}d.channel 2>/dev/null || echo "")
          if [ -n "${'$'}v" ] && [ "${'$'}v" != "auto" ] && [ "${'$'}v" != "0" ]; then
            echo "${'$'}d:${'$'}v"
          fi
        done
        """.trimIndent()
        ).trim()
        return out.isNotEmpty()
    }



    override suspend fun execute(): Boolean {
        val res = sh(
            """
        devs=${'$'}(uci -q show wireless | sed -n 's/^wireless\.\([^=]*\)=wifi-device/\1/p')
        changed=0
        for d in ${'$'}devs; do
          cur=${'$'}(uci -q get wireless.${'$'}d.channel 2>/dev/null || echo "")
          # si ya está auto/0 o vacío, no tocamos
          if [ -n "${'$'}cur" ] && [ "${'$'}cur" != "auto" ] && [ "${'$'}cur" != "0" ]; then
            uci set wireless.${'$'}d.channel='auto' || true
            changed=1
          fi
        done
        if [ "${'$'}changed" = "1" ]; then
          uci commit wireless
          wifi reload
        fi
        echo OK
        """.trimIndent()
        ).trim()

        return res.lines().lastOrNull() == "OK"
    }
}