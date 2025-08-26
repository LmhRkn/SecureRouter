package com.tfg.securerouter.data.router

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import com.tfg.securerouter.data.app.screens.router_selector.model.RouterInfo
import com.tfg.securerouter.data.json.jsons.router_selector.RouterSelectorCache
import com.tfg.securerouter.data.utils.AppSession
import com.tfg.securerouter.data.utils.resolveStoredPassword
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun connectSSH(
    username: String,
    passwordEncrypted: String?,
    host: String,
    port: Int = 22,
    command: String,
    jschSocketFactory: JschSocketFactory? = null,
): String {
    val password: String? = resolveStoredPassword(passwordEncrypted) ?: passwordEncrypted
    fun escSingleQuotes(s: String) = s.replace("'", "'\"'\"'")
    val finalCmd = "sh -lc '${escSingleQuotes(command)}'"

    Log.d("connectSSH", "user=$username host=$host viaVPN=${jschSocketFactory != null} cmd=$command")

    var session: Session? = null
    var channel: ChannelExec? = null
    val errBuf = java.io.ByteArrayOutputStream()
    val t0 = System.currentTimeMillis()

    fun newSession(): Session {
        val jsch = JSch().apply { removeAllIdentity() }
        return jsch.getSession(username, host, port).apply {
            if (password != null) setPassword(password)
            val config = Properties().apply {
                put("StrictHostKeyChecking", "no")
                put("PreferredAuthentications", "password")
                // Opcionalmente:
                // put("ServerAliveInterval", "10")  // algunos JSch no leen esto; usamos API abajo
            }
            setConfig(config)
            if (jschSocketFactory != null) setSocketFactory(jschSocketFactory)
            setServerAliveInterval(10_000)      // keepalive
            setServerAliveCountMax(3)           // cae tras ~30s sin respuesta

            val tConn0 = System.currentTimeMillis()
            connect(30_000)
            Log.d("connectSSH", "session.connect in ${System.currentTimeMillis() - tConn0}ms")

            // guarda en pool
            SshSessionPool.put(username, host, port, jschSocketFactory, this)
        }
    }

    var attempt = 0
    while (true) {
        try {
            session = SshSessionPool.get(username, host, port, jschSocketFactory) ?: newSession()

            channel = (session.openChannel("exec") as ChannelExec).apply {
                setPty(false)
                setCommand(finalCmd)
                inputStream = null
                setErrStream(errBuf)
                val tCh0 = System.currentTimeMillis()
                connect(10_000)
                Log.d("connectSSH", "channel.connect in ${System.currentTimeMillis() - tCh0}ms")
            }

            // 3) leer salida
            val tRead0 = System.currentTimeMillis()
            val out = channel.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            while (!channel.isClosed) Thread.sleep(15)
            val exit = channel.exitStatus
            val err = errBuf.toString(Charsets.UTF_8)

            Log.d(
                "connectSSH",
                "read+wait in ${System.currentTimeMillis() - tRead0}ms; total=${System.currentTimeMillis() - t0}ms; exit=$exit"
            )

            return if (exit == 0) out
            else "Exit $exit\nSTDERR:\n$err\nSTDOUT:\n$out"
        } catch (e: JSchException) {
            val msg = e.message ?: "JSchException"
            // Si es auth o sesión rota, limpia del pool y (si es el 1º intento) reintenta
            val retriable = msg.contains("session is down", true)
                    || msg.contains("channel is not opened", true)
                    || msg.contains("timeout", true)
                    || msg.contains("End of IO Stream Read", true)

            if (msg.contains("Auth fail", true)) {
                SshSessionPool.remove(username, host, port, jschSocketFactory)
                return "Error: autenticación fallida. Revisa PasswordAuth en el servidor o la contraseña."
            }

            if (retriable && attempt == 0) {
                attempt++
                SshSessionPool.remove(username, host, port, jschSocketFactory)
                runCatching { channel?.disconnect() }
                continue
            }

            return "Error: $msg"
        } catch (e: Exception) {
            return "Error: ${e.message}"
        } finally {
            try { channel?.disconnect() } catch (_: Exception) {}
        }
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
    val router: RouterInfo = RouterSelectorCache.getRouter(AppSession.routerId.toString())
        ?: return "ERROR"

    val host: String = when {
        router.isVpn -> "192.168.9.1"
        !router.localIp.isNullOrBlank() -> router.localIp
        !router.publicIpOrDomain.isNullOrBlank() -> router.publicIpOrDomain
        else -> AppSession.routerIp ?: ""
    }

    val jschSF = if (router.isVpn) {
        val net = VpnUtils.getActiveVpnNetwork() ?: VpnUtils.findAnyVpnNetwork()
        net?.let { AndroidNetworkSocketFactory(it) }
    } else null

    val port = router.vpnPort ?: 22

    val output = connectSSH(
        username = "root",
        passwordEncrypted = router.sshPassword,
        host = host,
        port = port,
        command = command,
        jschSocketFactory = jschSF
    )
    return output
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun sendCommandEphemeral(router: RouterInfo, command: String): String {
    val host: String = when {
        router.isVpn && !router.vpnHost.isNullOrBlank() -> router.vpnHost
        !router.localIp.isNullOrBlank() -> router.localIp
        !router.publicIpOrDomain.isNullOrBlank() -> router.publicIpOrDomain
        else -> return "Error: no host"
    }

    val jschSF: JschSocketFactory? = if (router.isVpn) {
        val net = VpnUtils.getActiveVpnNetwork() ?: VpnUtils.findAnyVpnNetwork()
        net?.let { AndroidNetworkSocketFactory(it) }
    } else null

    val port = router.vpnPort ?: 22

    Log.d("sendCommandEphemeral", "router: $router")
    return connectSSH(
        username = "root",
        passwordEncrypted = router.sshPassword,
        host = host,
        port = port,
        command = command,
        jschSocketFactory = jschSF
    )
}


suspend fun shUsingLaunch(
    command: String,
): String =
suspendCancellableCoroutine { cont ->
    val job = launchCommand(
        command = command,
        parse = { it },
        onResult = { out -> if (cont.isActive) cont.resume(out) },
        onError  = { e -> if (cont.isActive) cont.resumeWithException(e) }
    )
    cont.invokeOnCancellation { job.cancel() }
}

private object SshSessionPool {
    private data class Key(
        val user: String,
        val host: String,
        val port: Int,
        val sfId: Int
    )

    private val map = java.util.concurrent.ConcurrentHashMap<Key, Session>()

    fun get(user: String, host: String, port: Int, sf: JschSocketFactory?): Session? {
        val k = Key(user, host, port, sf?.hashCode() ?: 0)
        val s = map[k]
        return if (s?.isConnected == true) s else {
            if (s != null) map.remove(k)
            null
        }
    }

    fun put(user: String, host: String, port: Int, sf: JschSocketFactory?, s: Session) {
        val k = Key(user, host, port, sf?.hashCode() ?: 0)
        map[k] = s
    }

    fun remove(user: String, host: String, port: Int, sf: JschSocketFactory?) {
        val k = Key(user, host, port, sf?.hashCode() ?: 0)
        map.remove(k)?.let { runCatching { it.disconnect() } }
    }

    fun closeAll() {
        map.values.forEach { runCatching { it.disconnect() } }
        map.clear()
    }
}