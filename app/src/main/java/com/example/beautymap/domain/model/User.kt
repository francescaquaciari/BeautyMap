package com.example.beautymap.domain.model

data class User(
    val id: Int = 0,
    val name: String,
    val username: String,
    val email: String,
    val city: String,
    val address: String = "",
    val phone: String = "",
    val facebook: String = "",
    val website: String = "",
    val instagram: String = "",
    val lat: String,
    val lng: String
)
