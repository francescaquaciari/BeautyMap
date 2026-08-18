package com.example.beautymap.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val username: String,
    val email: String,
    val city: String,
    val lat: String,
    val lng: String
)
