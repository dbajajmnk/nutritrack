package com.mahbeermohammed.fit2081nutritrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodIntakeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(foodIntake: FoodIntake)

    @Query("SELECT * FROM FoodIntake WHERE userId = :userId")
    suspend fun getByUserId(userId: String): List<FoodIntake>

    @Query("SELECT * FROM FoodIntake WHERE userId = :userId ORDER BY id DESC LIMIT 1")
    suspend fun getLatestFoodIntakeForUser(userId: String): FoodIntake?

}

