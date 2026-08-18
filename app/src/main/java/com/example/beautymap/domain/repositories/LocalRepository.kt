package com.example.beautymap.domain.repositories

import com.example.beautymap.domain.model.User

interface LocalRepository {
    suspend fun save(data: List<User>)

    suspend fun getAll(): List<User>

    suspend fun clear()
}