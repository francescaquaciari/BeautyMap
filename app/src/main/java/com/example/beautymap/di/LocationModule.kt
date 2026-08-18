package com.example.beautymap.di

import android.content.Context
import com.example.beautymap.common.LocationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun providerLocation (@ApplicationContext context: Context) = LocationHelper(context)
}

