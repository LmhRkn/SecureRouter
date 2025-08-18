package com.tfg.securerouter.data.utils

import android.util.Base64
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val SECRET_TEXT = "tfg-demo-key-change-me" // cambia esta cadena
private const val TRANSFORM   = "AES/GCM/NoPadding"
private const val TAG_BITS    = 128
private const val IV_LEN      = 12
private val UTF8: Charset = Charsets.UTF_8

fun encryptPassword(plaintext: String): String {
    require(plaintext.isNotEmpty()) { "plaintext vacío" }

    val keyBytes = MessageDigest.getInstance("SHA-256")
        .digest(SECRET_TEXT.toByteArray(UTF8))
    val key = SecretKeySpec(keyBytes, "AES")

    val iv = ByteArray(IV_LEN).also { SecureRandom().nextBytes(it) }

    val cipher = Cipher.getInstance(TRANSFORM)
    cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(TAG_BITS, iv))
    val ct = cipher.doFinal(plaintext.toByteArray(UTF8))

    val ivB64 = Base64.encodeToString(iv, Base64.NO_WRAP)
    val ctB64 = Base64.encodeToString(ct, Base64.NO_WRAP)
    return "v1s.$ivB64.$ctB64"
}

fun decryptPassword(encoded: String): String {
    val parts = encoded.split('.')
    require(parts.size == 3 && parts[0] == "v1s") { "formato inválido" }

    val iv = Base64.decode(parts[1], Base64.NO_WRAP)
    val ct = Base64.decode(parts[2], Base64.NO_WRAP)

    val keyBytes = MessageDigest.getInstance("SHA-256")
        .digest(SECRET_TEXT.toByteArray(UTF8))
    val key = SecretKeySpec(keyBytes, "AES")

    val cipher = Cipher.getInstance(TRANSFORM)
    cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_BITS, iv))
    val pt = cipher.doFinal(ct)
    return pt.toString(UTF8)
}

object PasswordGenerator {
    private val rng = SecureRandom()
    private val SIMBOLOS = "!@#\$%&*+-_=:.?,;".toCharArray()
    private val SIMILARES = setOf('O','0','o','1','l','I','|','`','\'','"',' ')

    fun generar(
        longitud: Int = 16,
        minus: Boolean = true,
        mayus: Boolean = true,
        digitos: Boolean = true,
        simbolos: Boolean = true,
        evitarSimilares: Boolean = true,
        exigirCadaClase: Boolean = true
    ): String {
        val clases = mutableListOf<CharArray>()

        if (minus)  clases += ('a'..'z').filterNot { evitarSimilares && it in SIMILARES }.toCharArray()
        if (mayus)  clases += ('A'..'Z').filterNot { evitarSimilares && it in SIMILARES }.toCharArray()
        if (digitos) clases += ('0'..'9').filterNot { evitarSimilares && it in SIMILARES }.toCharArray()
        if (simbolos) clases += SIMBOLOS

        require(clases.isNotEmpty()) { "Activa al menos un conjunto de caracteres." }
        require(!exigirCadaClase || longitud >= clases.size) {
            "La longitud es menor que el nº de conjuntos activos."
        }

        val alfabeto = clases.flatMap { it.asList() }.toCharArray()
        val pwd = ArrayList<Char>(longitud)

        if (exigirCadaClase) {
            for (grupo in clases) {
                pwd += grupo[rng.nextInt(grupo.size)]
            }
        }

        while (pwd.size < longitud) {
            val c = alfabeto[rng.nextInt(alfabeto.size)]
            val n = pwd.size
            if (n >= 2 && pwd[n-1] == c && pwd[n-2] == c) continue
            pwd += c
        }

        for (i in pwd.size - 1 downTo 1) {
            val j = rng.nextInt(i + 1)
            val t = pwd[i]; pwd[i] = pwd[j]; pwd[j] = t
        }

        return pwd.joinToString("")
    }
}
