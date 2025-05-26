package com.mahbeermohammed.fit2081nutritrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FoodIntake(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val persona: String,
    val breakfastTime: String,
    val lunchTime: String,
    val dinnerTime: String,
    val fruit: String,
    val vegetables: String,
    val grains: String,
    val meat: String,
    val dairy: String
)
