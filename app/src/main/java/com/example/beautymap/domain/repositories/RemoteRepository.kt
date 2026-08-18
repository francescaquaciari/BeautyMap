package com.example.beautymap.domain.repositories

import com.example.beautymap.domain.model.User

interface RemoteRepository  {
    suspend fun downloadData() : List<User>
}