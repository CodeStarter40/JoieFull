package com.openclassrooms.joiefull.data.api

import retrofit2.http.GET

interface InterfaceApi {
    @GET("main/api/clothes.json")
    suspend fun getItems(): List<ItemResponse>

}