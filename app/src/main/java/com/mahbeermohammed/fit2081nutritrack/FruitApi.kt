package com.mahbeermohammed.fit2081nutritrack.data

import retrofit2.http.GET
import retrofit2.http.Path

interface FruitApi {
    @GET("api/fruit/{name}")
    suspend fun getFruit(@Path("name") fruitName: String): FruitInfo
}
