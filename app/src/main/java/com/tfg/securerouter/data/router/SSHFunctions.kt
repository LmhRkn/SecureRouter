package com.tfg.securerouter.data.router

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

/**
 * Establishes an SSH connection to a remote host and executes a given command.
 * Notes:
 *  * - Host key checking is disabled for convenience (`StrictHostKeyChecking = no`).
 *  * - This method is blocking and should be called from a background thread (e.g., via Dispatchers.IO).
 *
 * @param username The SSH username [String] (e.g., "root").
 * @param password The SSH password [String] for authentication.
 * @param host The IP address or hostname [String] of the remote SSH server.
 * @param port The port number [Int] for the SSH connection (default is 22).
 * @param command The shell command [String] to be executed on the remote host.
 *
 * @return The output of the executed command as a [String], or an error message if the connection fails.
 *
 * @throws Exception Prints stack trace on failure and returns the exception message in the output.
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
 * Sends a shell command to a predefined router via SSH and returns the result.
 *
 * Uses fixed credentials and host (root@192.168.2.104).
 *
 * @param command The shell command [String] to send to the router.
 * @return The output from the SSH command execution [String].
 *
 * @see connectSSH for internal connection logic.
 */
fun sendCommand(command: String): String {
    val output = connectSSH(
        username = "root",
        password = "SecureRouter",
        host = "192.168.2.104",
        command = command
    )
    return output
}