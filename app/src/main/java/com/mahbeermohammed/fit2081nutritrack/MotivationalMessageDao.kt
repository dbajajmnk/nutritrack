package com.mahbeermohammed.fit2081nutritrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MotivationalMessageDao {
    @Insert
    suspend fun insert(message: MotivationalMessage)

    @Query("SELECT * FROM motivational_messages ORDER BY timestamp DESC")
    suspend fun getAllMessages(): List<MotivationalMessage>

    @Query("SELECT * FROM motivational_messages ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessage(): MotivationalMessage?
}