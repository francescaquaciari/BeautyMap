package com.example.beautymap.data.remote.model

import com.example.beautymap.data.remote.service.EndpointService
import com.example.beautymap.domain.model.User
import com.example.beautymap.domain.repositories.RemoteRepository
import jakarta.inject.Inject

// 1. Questa è la nuova funzione che trasforma ogni "Feature" del GeoJSON in un vostro "User"
private fun Feature.toDomain(): User {
    // Generiamo un ID numerico finto partendo dal nome dell'estetista per non rompere il "id.toLong()"
    val idFinto = properties.name?.hashCode()?.toLong() ?: (1000..9999).random().toLong()

    return User(
        id = idFinto.toInt(),
        name = properties.name ?: "Centro Estetico",
        username = properties.name ?: "beauty",
        email = "info@estetica.it",
        // Usiamo la proprietà calcolata che unisce i tag disponibili o mette una stringa di default
        city = properties.indirizzoCompleto,
        // Usiamo i getter personalizzati 'latitude' e 'longitude' che estraggono i dati dai poligoni
        lat = geometry.latitude.toString(),
        lng = geometry.longitude.toString()
    )
}

class RetrofitRemoteRepository @Inject constructor(
    private val service: EndpointService
) : RemoteRepository {

    // 2. Questa funzione rimane IDENTICA nella firma, cambia solo da dove prende i dati
    override suspend fun downloadData(): List<User> {
        // service.cercaEstetisteVicine() scarica il GeoJSON, poi prendiamo le sue features e le mappiamo
        return service.cercaEstetisteVicine().features.map { it.toDomain() }
    }
}