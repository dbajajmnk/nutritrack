package com.mahbeermohammed.fit2081nutritrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PatientDao {

    // Used on app start to check if Room DB is empty
    @Query("SELECT COUNT(*) FROM patients")
    suspend fun getPatientCount(): Int

    // Insert patients from CSV on first launch
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(patients: List<Patient>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(patient: Patient)

    // Used in login screen to fetch patient by userId
    @Query("SELECT * FROM patients WHERE userId = :userId")
    suspend fun getPatientById(userId: String): Patient?

    // Used in claim account screen to update name and password
    @Update
    suspend fun updatePatient(patient: Patient)
}
