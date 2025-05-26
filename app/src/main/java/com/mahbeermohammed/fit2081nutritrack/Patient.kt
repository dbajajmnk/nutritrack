package com.mahbeermohammed.fit2081nutritrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey val userId: String,
    val phoneNumber: String,
    val sex: String,
    val name: String? = null,
    val password: String? = null
    // Add other fields here if needed
)
