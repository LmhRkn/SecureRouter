package com.tfg.securerouter.data.app.screens.wifi.model.send.vpn

import com.tfg.securerouter.data.router.launchCommand
import com.tfg.securerouter.data.utils.AppSession

object AddVPNPeer {
    fun addVPNPeer(domain: String, name: String) {
        val BASE = "/root/vpn"
        val DIR = "$BASE/router"
        val DEV = "$BASE/devices/peer_${AppSession.nextDeviceVPN}"

        val SERVER_PUB = "$DIR/wgserver.pub"
        val CLIENT_PUB = "$DEV/wgclient.pub"
        val CLIENT_KEY = "$DEV/wgclient.key"
        val CLIENT_PSK = "$DEV/wgclient.psk"

        val CLIENT_IP  = "192.168.9.${AppSession.nextDeviceVPN}/32"
        val CLIENT_IP6 = "fd00:9::${AppSession.nextDeviceVPN}/128"

        val VPN_IF = "vpn"
        val VPN_PORT = "51820"

        val command = """
            mkdir -p $DEV
            umask go=
            wg genkey | tee $CLIENT_KEY | wg pubkey > $CLIENT_PUB
            wg genpsk > $CLIENT_PSK
            
            sleep 2
            
            # Configurar peer en el servidor (UCI)
            uci -q delete network.$name
            uci set network.$name="wireguard_$VPN_IF"
            uci set network.$name.public_key="${'$'}(cat $CLIENT_PUB)"
            uci set network.$name.preshared_key="${'$'}(cat $CLIENT_PSK)"
            uci add_list network.$name.allowed_ips="$CLIENT_IP"
            uci add_list network.$name.allowed_ips="$CLIENT_IP6"
            uci set network.$name.description="$name"
            uci commit network
           
            /etc/init.d/network reload
            ifdown $VPN_IF 2>/dev/null || true
            ifup $VPN_IF

            # Generar client.conf
            cat > $DEV/client.conf <<EOF
            [Interface]
            PrivateKey = ${'$'}(cat $CLIENT_KEY)
            Address = $CLIENT_IP, $CLIENT_IP6
            DNS = ${AppSession.routerIp}

            [Peer]
            PublicKey = ${'$'}(cat $SERVER_PUB)
            PresharedKey = ${'$'}(cat $CLIENT_PSK)
            Endpoint = $domain:$VPN_PORT
            AllowedIPs = 0.0.0.0/0, ::/0
            PersistentKeepalive = 25
            EOF

            # Permisos (por si acaso)
            chmod 600 $CLIENT_KEY $CLIENT_PSK $CLIENT_PUB
        """.trimIndent()

        launchCommand(
            command = command,
            parse = { output -> output.isNotBlank() },
            onResult = {}
        )
    }
}
