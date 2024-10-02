package com.openclassrooms.joiefull.data.api

import retrofit2.http.GET

interface ClothesApiService {
    @GET("main/api/clothes.json")

    suspend fun getItems(): List<ClothesItem>

}