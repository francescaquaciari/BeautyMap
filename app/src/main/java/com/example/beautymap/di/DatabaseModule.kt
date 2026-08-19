package com.example.beautymap.di

import android.content.Context
import androidx.room.Room
import com.example.beautymap.data.local.AppDatabase
import com.example.beautymap.data.local.RoomLocalRepository
import com.example.beautymap.domain.repositories.LocalRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)
    = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule{
    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        roomLocalRepository: RoomLocalRepository
    ): LocalRepository

}