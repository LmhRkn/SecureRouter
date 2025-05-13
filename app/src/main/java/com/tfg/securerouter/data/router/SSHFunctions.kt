package com.tfg.securerouter.data.router

import com.jcraft.jsch.JSch
import com.jcraft.jsch.Session

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
        val jsch = JSch()
        session = jsch.getSession(username, host, port)
        session.setPassword(password)

        val config = java.util.Properties()
        config["StrictHostKeyChecking"] = "no"
        session.setConfig(config)

        session.connect(30000) // Tiempo de espera

        val channel = session.openChannel("exec") as com.jcraft.jsch.ChannelExec
        channel.setCommand(command)
        channel.inputStream = null

        val input = channel.inputStream
        channel.connect()

        output = input.bufferedReader().use { it.readText() }

        channel.disconnect()
    } catch (e: Exception) {
        e.printStackTrace()
        output = "Error: ${e.message}"
    } finally {
        session?.disconnect()
    }

    return output
}

fun sendCommand(command: String): String {
    val output = connectSSH(
        username = "root",
        password = "SecureRouter",
        host = "192.168.1.1",
        command = command
    )
    return output
}