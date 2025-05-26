package com.mahbeermohammed.fit2081nutritrack.data

class FruitRepository {
    suspend fun getFruitInfo(name: String): FruitInfo = RetrofitInstance.api.getFruit(name)
}
