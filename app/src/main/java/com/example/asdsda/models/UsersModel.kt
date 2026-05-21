package com.example.asdsda.models

data class UsersModel (
    val email : String = "",
    val uid : String = "",
    val cartItems : Map<String,Long> = emptyMap(),
    val userId: String = "",
)


