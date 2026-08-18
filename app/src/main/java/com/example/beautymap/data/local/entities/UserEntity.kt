package com.example.beautymap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey // Senza auto-generate se l'ID arriva dal server
    val id: Long? = null,
    val email: String,
    val name: String,
    val username: String,
    val city: String,
    val lat: Double,
    val lng: Double,
    val createdAt: Long = System.currentTimeMillis()
)