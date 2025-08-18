package com.tfg.securerouter.data.router

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.json.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.decryptPassword
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Job
import java.util.Properties
import com.jcraft.jsch.SocketFactory as JschSocketFactory

/**
 * Establishes an SSH connection to a remote host and executes a single command.
 *
 * Usage:
 * This function creates an SSH session using the provided credentials, runs the given [command],
 * and returns its output as a [String]. It disables strict host key checking for simplicity.
 *
 * Notes:
 * - The function is private and intended for internal use.
 * - Uses a 30-second timeout for session connection.
 * - Automatically closes the session and channel after execution.
 *
 * @param username The SSH username for authentication.
 * @param password The SSH password for authentication.
 * @param host The IP address or hostname of the SSH server.
 * @param port The SSH port. Defaults to `22`.
 * @param command The shell command to execute on the remote host.
 * @return The standard output of the executed command, or an error message if execution fails.
 */

private fun connectSSH(
    username: String,
    password: String,
    host: String,
    port: Int = 22,
    command: String,
    jschSocketFactory: JschSocketFactory? = null
): String {
    var session: Session? = null
    var channel: ChannelExec? = null
    val errBuf = ByteArrayOutputStream()

    return try {
        val jsch = JSch().apply {
            // Asegura que NO haya identidades cargadas → no intente "publickey"
            removeAllIdentity()
        }

        session = jsch.getSession(username, host, port).apply {
            setPassword(password)

            val config = Properties().apply {
                put("StrictHostKeyChecking", "no")
                // Fuerza orden de métodos: primero contraseña; no ofrecemos publickey
                put("PreferredAuthentications", "password,keyboard-interactive")
            }
            setConfig(config)

            if (jschSocketFactory != null) setSocketFactory(jschSocketFactory)

            // Opcional: keepalive por si hay NATs quisquillosos
            setServerAliveInterval(10_000)

            connect(30_000)
        }

        channel = (session.openChannel("exec") as ChannelExec).apply {
            setCommand(command)
            setInputStream(null)           // no input interactivo
            setErrStream(errBuf)           // captura STDERR
            connect(10_000)
        }

        val out = channel.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }

        // Espera a que el comando cierre para vaciar buffers
        while (!channel.isClosed) Thread.sleep(30)
        val exit = channel.exitStatus
        val err  = errBuf.toString(Charsets.UTF_8)

        if (exit == 0) out
        else "Exit $exit\nSTDERR:\n$err\nSTDOUT:\n$out"
    } catch (e: JSchException) {
        // Caso clásico: el servidor SOLO permite 'publickey'
        if (e.message?.contains("Auth fail", ignoreCase = true) == true)
            "Error: autenticación fallida. El servidor puede tener PasswordAuth desactivado o la contraseña es incorrecta. " +
                    "En OpenWrt: uci set dropbear.@dropbear[0].PasswordAuth='on'; uci set dropbear.@dropbear[0].RootPasswordAuth='on'; uci commit; /etc/init.d/dropbear reload"
        else "Error: ${e.message}"
    } catch (e: Exception) {
        "Error: ${e.message}"
    } finally {
        try { channel?.disconnect() } catch (_: Exception) {}
        try { session?.disconnect() } catch (_: Exception) {}
    }
}


/**
 * Sends a shell command to the router via SSH using predefined credentials and host.
 *
 * Usage:
 * This function is a convenience wrapper around [connectSSH] that executes the specified
 * [command] on a router and returns its standard output as a [String].
 *
 * @param command The shell command to execute on the router.
 * @return The standard output from the command execution, or an error message if the connection fails.
 *
 * @see connectSSH
 */

fun sendCommand(command: String): String {
    val router: RouterInfo? = RouterSelectorCache.getRouter(AppSession.routerId.toString())
    if (router == null) return "ERROR"


    val host: String = when {
        router.isVpn && !router.vpnHost.isNullOrBlank() -> router.vpnHost
        !router.localIp.isNullOrBlank() -> router.localIp
        !router.publicIpOrDomain.isNullOrBlank() -> router.publicIpOrDomain
        else -> AppSession.routerIp ?: ""
    } ?: ""

    val jschSF = if (router.isVpn) {
        val net = VpnUtils.getActiveVpnNetwork() ?: VpnUtils.findAnyVpnNetwork()
        net?.let { AndroidNetworkSocketFactory(it) }
    } else null

    val port = router.vpnPort ?: 22

    val output = connectSSH(
        username = "root",
        password = if (router.sshPassword != null) decryptPassword(router.sshPassword) else "",
        host = host,
        port = port,
        command = command,
        jschSocketFactory = jschSF
    )
    println("host=$host viaVPN=${jschSF!=null} cmd=$command\n$output")
    return output
}

fun sendCommandEphemeral(router: RouterInfo, command: String): String {
    val host: String = when {
        router.isVpn && !router.vpnHost.isNullOrBlank() -> router.vpnHost
        !router.localIp.isNullOrBlank()                 -> router.localIp
        !router.publicIpOrDomain.isNullOrBlank()        -> router.publicIpOrDomain
        else -> return "Error: no host"
    }

    val jschSF: JschSocketFactory? = if (router.isVpn) {
        val net = VpnUtils.getActiveVpnNetwork() ?: VpnUtils.findAnyVpnNetwork()
        net?.let { AndroidNetworkSocketFactory(it) }
    } else null

    val port = router.vpnPort ?: 22

    return connectSSH(
        username = "root",
        password = "12345678",
        host = host,
        port = port,
        command = command,
        jschSocketFactory = jschSF
    )
}



suspend fun shUsingLaunch(
    command: String,
    timeoutMs: Long = 10_000L
): String = withTimeout(timeoutMs) {
    suspendCancellableCoroutine { cont ->
        val job: Job = launchCommand(
            command = command,
            parse   = { it },
            onResult = { out -> if (cont.isActive) cont.resume(out) },
            onError  = { e   -> if (cont.isActive) cont.resumeWithException(e) }
        )
        cont.invokeOnCancellation { job.cancel() }
    }
}