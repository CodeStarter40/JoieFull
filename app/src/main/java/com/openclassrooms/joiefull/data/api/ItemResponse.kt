package com.openclassrooms.joiefull.data.api



data class  Picture(
    val url: String,
    val description: String,
)

data class ItemResponse(
    val id: Int,
    val picture: Picture,
    val name: String,
    val price: Double,
    val quantity: Int,
    val original_price: Double,
    val like: Int,
    val category: String
)
