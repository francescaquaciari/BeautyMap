package com.example.beautymap.di

import com.example.beautymap.data.remote.service.EndpointService
import com.example.beautymap.data.remote.model.RetrofitRemoteRepository
import com.example.beautymap.domain.repositories.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides @Singleton
    fun provideClient():Retrofit {
        val interceptor = HttpLoggingInterceptor()                                    //intercettore per spiare le chiamate di rete
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)                       //imposta il livello di dettaglio dell'intercettore
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()       //crea il client di rete con l'intercettore

        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://pastebin.com")
            .addConverterFactory(GsonConverterFactory.create())                      //converte il JSON in oggetti Kotlin
            .build()
    }

    @Provides @Singleton
    fun endpointService(client: Retrofit) = client.create(EndpointService::class.java)            //crea una istanza di EndpointService

}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule {                                                           //serve per ottenere l'istanza del repository remoto

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        retrofitRemoteRepository: RetrofitRemoteRepository
    ): RemoteRepository
}
