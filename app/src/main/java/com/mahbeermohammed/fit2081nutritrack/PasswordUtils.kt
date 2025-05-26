package com.mahbeermohammed.fit2081nutritrack.utils

import java.security.MessageDigest

object PasswordUtils {
    fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
