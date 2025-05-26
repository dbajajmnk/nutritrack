package com.mahbeermohammed.fit2081nutritrack

import android.content.Context

fun loadPatientsFromCSV(context: Context): List<Patient> {
    val patients = mutableListOf<Patient>()
    val inputStream = context.assets.open("nutri_users.csv")
    val lines = inputStream.bufferedReader().readLines()

    for (line in lines.drop(1)) {
        val tokens = line.split(",")
        if (tokens.size >= 5) {
            patients.add(
                Patient(
                    phoneNumber = tokens[0].trim(),
                    userId = tokens[1].trim(),
                    sex = tokens[2].trim(),

                )
            )
        }
    }
    return patients
}
