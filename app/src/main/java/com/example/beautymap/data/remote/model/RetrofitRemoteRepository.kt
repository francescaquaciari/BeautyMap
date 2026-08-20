package com.example.beautymap.data.remote.model

import com.example.beautymap.data.remote.service.EndpointService
import com.example.beautymap.domain.model.User
import com.example.beautymap.domain.repositories.RemoteRepository
import jakarta.inject.Inject

// 1. Questa è la nuova funzione che trasforma ogni "Feature" del GeoJSON in un vostro "User"
private fun Feature.toDomain(): User {                                                            //converte un oggetto Feature in un oggetto User
    val stableId = id?.hashCode() ?: (properties.name ?: "").hashCode()                           //genera un id basandosi sul JSON

    return User(
        id = stableId,
        name = if (!properties.name.isNullOrBlank()) properties.name else "Centro Estetico",
        username = if (!properties.name.isNullOrBlank()) properties.name else "estetista",
        email = properties.email ?: "",
        city = properties.soloCitta,
        address = properties.soloIndirizzo,
        phone = properties.phone ?: "",
        facebook = properties.facebook ?: "",
        website = properties.website ?: "",
        instagram = properties.instagram ?: "",
        lat = geometry.latitude.toString(),
        lng = geometry.longitude.toString()
    )
}

class RetrofitRemoteRepository @Inject constructor(                                               //classe che implementa l'interfaccia RemoteRepository
    private val service: EndpointService
) : RemoteRepository {

    // 2. Questa funzione rimane IDENTICA nella firma, cambia solo da dove prende i dati
    override suspend fun downloadData(): List<User> {
        // service.cercaEstetisteVicine() scarica il GeoJSON, poi prendiamo le sue features e le mappiamo
        return service.cercaEstetisteVicine().features.map { it.toDomain() }
    }
}