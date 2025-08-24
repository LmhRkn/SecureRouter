package com.tfg.securerouter.data.automatization.automatizations.on_vpn

import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.automatization.AutomatizationDefault
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.notice.model.alerts.AlertSpec
import com.tfg.securerouter.data.notice.model.alerts.TextPromptSpec
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.PromptBus

class FirstVPNUser(
    private val sh: suspend (String) -> String,
) : AutomatizationDefault() {

    override val timeoutMs: Long = 30_000L

    override suspend fun shouldRun(router: RouterInfo?): Int {
        return if (AppSession.firstVPN) 1 else -1
    }

    private fun lastNonEmpty(out: String) =
        out.lineSequence().map { it.trim() }.lastOrNull { it.isNotEmpty() }.orEmpty()

    override suspend fun execute(): Boolean {
        AppSession.firstVPN = false
        val id = AppSession.routerId?.toString() ?: return false

        // 1) Detectar package manager
        val detectCmd = """
            { opkg --version >/dev/null 2>&1 && echo opkg; } \
            || { apk --version >/dev/null 2>&1 && echo apk; } \
            || { { [ -x /bin/opkg ] || [ -x /usr/bin/opkg ]; } && echo opkg; } \
            || { { [ -x /sbin/apk ] || [ -x /bin/apk ] || [ -x /usr/sbin/apk ] || [ -x /usr/bin/apk ]; } && echo apk; } \
            || echo none
        """.trimIndent()

        val pm = sh(detectCmd)
            .lineSequence()
            .map { it.trim() }
            .firstOrNull { it == "opkg" || it == "apk" }
            ?: "none"
        if (pm != "opkg" && pm != "apk") return false

        RouterSelectorCache.update(id) { r -> r.copy(installerPackage = pm) }
        AppSession.packageInstaller = pm

        // 2) Pedir el dominio por texto
        val typed = PromptBus.textOrDefault(
            DomainTextSpec(
                default = (AppSession.ddnsDomain ?: ""),
            ),
            default = (AppSession.ddnsDomain ?: ""),
            timeoutMs = 60_000
        )?.trim().orEmpty()

        // Sanitizar: quitar esquemas y barras
        val domain = typed
            .removePrefix("http://")
            .removePrefix("https://")
            .trimEnd('/')
            .trim()

        if (domain.isEmpty()) {
            PromptBus.confirmOrDefault(DomainMissingSpec, default = false, timeoutMs = 15_000)
            return false
        }

        // Confirmación rápida para evitar typos
        val confirmed = PromptBus.confirmOrDefault(
            DomainConfirmSpec(domain),
            default = true,
            timeoutMs = 60_000
        )
        if (!confirmed) return false

        AppSession.ddnsDomain = domain

        val script = """
            set -e

            VPN_IF="vpn"
            VPN_PORT="51820"
            VPN_NET4_BASE="192.168.9"
            VPN_NET6_BASE="fd00:9::"
            ROUTER_DNS_VPN="192.168.9.1"

            BASE="/root/vpn"
            PEERS="${'$'}BASE/peers"
            SRVDIR="${'$'}BASE/router"

            mkdir -p "${'$'}PEERS"
            chmod 700 "${'$'}BASE" "${'$'}PEERS"

            [ -s "${'$'}SRVDIR/wgserver.key" ] && [ -s "${'$'}SRVDIR/wgserver.pub" ] || {
              echo "[keys] faltan wgserver.key/wgserver.pub en ${'$'}SRVDIR"; exit 1; }

            next_n() {
              local max=1
              for d in "${'$'}PEERS"/peer*; do
                [ -d "${'$'}d" ] || continue
                b="$(basename "${'$'}d")"
                n="${'$'}{b#peer}"
                case "${'$'}n" in ''|*[!0-9]*) continue;; esac
                if [ "${'$'}n" -gt "${'$'}max" ]; then max="${'$'}n"; fi
              done
              echo $((max + 1))
            }

            N="$(next_n)"
            [ "${'$'}N" -lt 2 ] && N=2
            if [ "${'$'}N" -gt 254 ]; then echo "Max peers 254 alcanzado"; exit 1; fi

            PEER_NAME="peer${'$'}N"
            PEER_DIR="${'$'}PEERS/${'$'}PEER_NAME"

            if uci -q show network | grep -q "^network\.${'$'}PEER_NAME="; then
              echo "network.${'$'}PEER_NAME ya existe. Aborta."; exit 1
            fi

            mkdir -p "${'$'}PEER_DIR"
            chmod 700 "${'$'}PEER_DIR"
            cd "${'$'}PEER_DIR"

            umask go=
            wg genkey | tee wgclient.key | wg pubkey > wgclient.pub
            wg genpsk > wgclient.psk
            chmod 600 wgclient.key wgclient.psk

            CLIENT_PRIV="$(cat wgclient.key)"
            CLIENT_PUB="$(cat wgclient.pub)"
            CLIENT_PSK="$(cat wgclient.psk)"

            CLIENT_IP="${'$'}VPN_NET4_BASE.${'$'}N/32"
            CLIENT_IP6="${'$'}VPN_NET6_BASE${'$'}N/128"

            uci -q delete network.${'$'}PEER_NAME || true
            uci set network.${'$'}PEER_NAME="wireguard_${'$'}{VPN_IF}"
            uci set network.${'$'}PEER_NAME.public_key="${'$'}CLIENT_PUB"
            uci set network.${'$'}PEER_NAME.preshared_key="${'$'}CLIENT_PSK"
            uci -q delete network.${'$'}PEER_NAME.allowed_ips || true
            uci add_list network.${'$'}PEER_NAME.allowed_ips="${'$'}CLIENT_IP"
            uci add_list network.${'$'}PEER_NAME.allowed_ips="${'$'}CLIENT_IP6"
            uci commit network

            /etc/init.d/network reload || true
            ifup "${'$'}VPN_IF" || true

            cat > client.conf <<EOF
[Interface]
PrivateKey = ${'$'}CLIENT_PRIV
Address = ${'$'}CLIENT_IP, ${'$'}CLIENT_IP6
DNS = ${'$'}ROUTER_DNS_VPN

[Peer]
PublicKey = $(cat "${'$'}SRVDIR/wgserver.pub")
PresharedKey = ${'$'}CLIENT_PSK
Endpoint = ${'$'}DOMAIN_PLACEHOLDER:${'$'}VPN_PORT
AllowedIPs = 0.0.0.0/0, ::/0
PersistentKeepalive = 25
EOF

            echo ""
            echo "================= QR (${ '$'}PEER_NAME ) ================="
            qrencode -t ansiutf8 < client.conf || echo "(qrencode no disponible)"
            echo "=========================================================="
            echo ""
            echo "OK ${'$'}PEER_NAME ${'$'}N ${'$'}PEER_DIR"
        """.trimIndent()
            .replace("\${'$'}DOMAIN_PLACEHOLDER", domain)

        val result = sh(script)
        val ok = result.lineSequence().any { it.trim().startsWith("OK ") }

        if (ok) {
            PromptBus.confirmOrDefault(QRSpec, default = true, timeoutMs = 120_000)
        }

        return ok
    }

    companion object {
        // Prompt de texto para pedir el dominio/DDNS
        fun DomainTextSpec(default: String) = TextPromptSpec(
            title = "WireGuard — Dominio/Endpoint",
            message = """
Escribe el **dominio o DDNS** (o IP pública) del router:
Ejemplos: mi-router.ddns.net, vpn.midominio.com, 203.0.113.45
            """.trimIndent(),
            placeholder = "tu-dominio.ddns.net",
            initialText = default,
            confirmText = "Usar este endpoint",
            cancelText = "Cancelar",
            showCancel = true
        )

        // Confirmación del endpoint escrito
        fun DomainConfirmSpec(domain: String) = AlertSpec(
            title = "WireGuard — Confirmar endpoint",
            message = "Se usará este endpoint para el cliente:\n\n    $domain:51820\n\n¿Es correcto?",
            confirmText = "Sí, continuar",
            cancelText = "Cancelar",
            showCancel = true
        )

        val DomainMissingSpec = AlertSpec(
            title = "WireGuard — Falta el endpoint",
            message = "No se ha introducido ningún dominio/DDNS. Operación cancelada.",
            confirmText = "Entendido",
            cancelText = "",
            showCancel = false
        )

        val QRSpec = AlertSpec(
            title = "WireGuard — Escanea el QR",
            message = """
Se ha creado el usuario VPN y su archivo client.conf.
En la consola del router verás el **código QR**.

📷 Por favor, **haz una foto** o **escanéalo** con la app WireGuard del dispositivo.
También puedes descargar el archivo con:
  scp root@<IP_DEL_ROUTER>:/root/vpn/peers/peerN/client.conf .
            """.trimIndent(),
            confirmText = "Hecho",
            cancelText = "",
            showCancel = false
        )
    }
}
