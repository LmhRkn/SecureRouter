package com.tfg.securerouter.data.router

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session
import com.tfg.securerouter.data.router.getRouterIpAddress
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.Job

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
    command: String
): String {
    var session: Session? = null
    var output = ""

    try {
        // Initialize JSch instance to handle SSH connection
        val jsch = JSch()

        // Create an SSH session with the given credentials and host
        session = jsch.getSession(username, host, port)
        session.setPassword(password)

        // Disable strict host key checking to avoid trust prompt
        val config = java.util.Properties()
        config["StrictHostKeyChecking"] = "no"
        session.setConfig(config)

        // Connect to the SSH session with a 30-second timeout
        session.connect(30000)

        // Open a new "exec" channel to execute a single command
        val channel = session.openChannel("exec") as com.jcraft.jsch.ChannelExec

        // Set the command to be executed remotely
        channel.setCommand(command)

        // Disable the input stream (not sending data to the remote process)
        channel.inputStream = null

        // Get the output stream of the remote process
        val input = channel.inputStream

        // Start the channel (command execution begins)
        channel.connect()

        // Read the command output into a String
        output = input.bufferedReader().use { it.readText() }

        // Disconnect the channel after command execution is complete
        channel.disconnect()
    } catch (e: Exception) {
        // Print the error to the console and return it as part of the output
        e.printStackTrace()
        output = "Error: ${e.message}"
    } finally {
        // Always disconnect the session to free resources
        session?.disconnect()
    }

    return output
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
    val ipHost: String = getRouterIpAddress()!!
    val output = connectSSH(
        username = "root",
        password = "SecureRouter",
        host = ipHost,
        command = command
    )
    println("command: $command\noutput: $output")
    return output
}

suspend fun shUsingLaunch(
    command: String,
    timeoutMs: Long = 10_000L
): String = withTimeout(timeoutMs) {
    suspendCancellableCoroutine { cont ->
        // Versión recomendada de launchCommand (ver más abajo) que devuelve Job:
        val job: Job = launchCommand(
            command = command,
            parse   = { it },              // String -> String (identidad)
            onResult = { out -> if (cont.isActive) cont.resume(out) },
            onError  = { e   -> if (cont.isActive) cont.resumeWithException(e) }
        )
        cont.invokeOnCancellation { job.cancel() }
    }
}