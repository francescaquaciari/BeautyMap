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
@InstallIn(SingletonComponent::class)                  //questi oggetti devono vivere per tutta la durata dell'app
object DatabaseModule {

    @Provides
    @Singleton                                                  //una sola istanza del database per tutta l'app
    fun provideDatabase(@ApplicationContext context: Context)
    = Room.databaseBuilder(
        context,
        AppDatabase::class.java,                         //classe che rappresenta il database
        "app_database"
    ).fallbackToDestructiveMigration(false).build()   //se cambia la versione del database, elimina i dati e crea uno nuovo

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()     //serve per ottenere l'istanza del DAO (interfaccia che comunica con il database)
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule {                               //serve per ottenere l'istanza del repository locale
    @Binds                                                              //quando uno UseCase richiede un repository locale, restituisce questa implementazione
    @Singleton
    abstract fun bindLocalRepository(
        roomLocalRepository: RoomLocalRepository
    ): LocalRepository
}