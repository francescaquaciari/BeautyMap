package com.example.beautymap.data.remote

import kotlinx.serialization.SerialName

data class GeoJsonResponse(
    val features: List<Feature>
)


data class Feature(
    val geometry: GeoJsonGeometry,
    val properties: FeatureProperties
)

data class GeoJsonGeometry(
    val coordinates: List<Double> // [Longitudine, Latitudine]
) {
    // Funzioni di utilità per non confondersi dopo
    val longitude: Double get() = coordinates.getOrNull(0) ?: 0.0
    val latitude: Double get() = coordinates.getOrNull(1) ?: 0.0
}

data class FeatureProperties(
    @SerialName("name") val name: String? = "Centro Estetico",
    @SerialName("addr:street") val street: String? = null,
    @SerialName("addr:housenumber") val houseNumber: String? = null
) {
    // Unisce via e numero civico per fare l'indirizzo completo
    val indirizzoCompleto: String
        get() = when {
            street != null && houseNumber != null -> "$street, $houseNumber"
            street != null -> street
            else -> "Indirizzo non disponibile"
        }
}