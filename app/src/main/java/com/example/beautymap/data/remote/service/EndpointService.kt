package com.example.beautymap.data.remote.service

import com.example.beautymap.data.remote.model.GeoJsonResponse
import retrofit2.http.GET

interface EndpointService {
    @GET("/raw/z20ETax9")
    suspend fun cercaEstetisteVicine(
    ): GeoJsonResponse // Questo sarà il nuovo modello di dati che Google ti restituisce
}