package com.example.asdsda.dataclasses

data class RestaurantInfo(
    val name: String,
    val deliveryInfo: String,
    val deliveryTime: String,
    val rating: Double,
    val reviewsCount: Int,
    val restImage: Int,
    val route : String
)
