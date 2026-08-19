package com.example.beautymap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.beautymap.data.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 3, exportSchema = false)    // version è importante per le migrazioni
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
}