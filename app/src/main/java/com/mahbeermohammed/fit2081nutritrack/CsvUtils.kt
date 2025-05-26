package com.mahbeermohammed.fit2081nutritrack

import android.content.Context

fun loadUsersFromCSV(context: Context): List<User> {
    val users = mutableListOf<User>()
    val inputStream = context.assets.open("nutri_users.csv")
    val lines = inputStream.bufferedReader().readLines()

    for (line in lines.drop(1)) { // Skip the header
        val tokens = line.split(",")
        if (tokens.size >= 5) {
            val user = User(
                phoneNumber = tokens[0].trim(),
                userId = tokens[1].trim(),
                sex = tokens[2].trim(),
                totalScoreMale = tokens[3].toDoubleOrNull() ?: 0.0,
                totalScoreFemale = tokens[4].toDoubleOrNull() ?: 0.0
            )
            users.add(user)
        }
    }

    return users
}