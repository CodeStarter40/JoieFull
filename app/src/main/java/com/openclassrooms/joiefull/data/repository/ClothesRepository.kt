package com.openclassrooms.joiefull.data.repository

import android.util.Log
import com.openclassrooms.joiefull.data.api.ClothesApiService
import com.openclassrooms.joiefull.data.api.ClothesItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClothesRepository @Inject constructor (private val apiService: ClothesApiService) {

    suspend fun getItems(): Flow<List<ClothesItem>> = flow {
        val items = apiService.getItems()
        emit(items)
    }
}
