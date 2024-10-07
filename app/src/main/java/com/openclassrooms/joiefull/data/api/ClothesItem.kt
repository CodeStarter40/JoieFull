package com.openclassrooms.joiefull.data.api



data class  Picture(
    val url: String,
    val description: String,
)

data class ClothesItem(
    val id: Int,
    val picture: Picture,
    val name: String,
    val price: Double,
    val quantity: Int,
    val original_price: Double,
    val likes: Int,
    val category: String
)
