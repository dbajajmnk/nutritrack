package com.mahbeermohammed.fit2081nutritrack.data

data class FruitInfo(
    val name: String,
    val id: Int,
    val family: String,
    val order: String,
    val genus: String,
    val nutritions: Nutritions
)

data class Nutritions(
    val calories: Int,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Int
)
