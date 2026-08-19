package com.example.beautymap.data.remote

import com.google.gson.annotations.SerializedName

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
    @SerializedName("name") val name: String? = "Centro Estetico",
    @SerializedName("beauty") val beauty: String? = null,
    @SerializedName("shop") val shop: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("addr:street") val street: String? = null,
    @SerializedName("addr:city") val city: String? = null,
    @SerializedName("addr:housenumber") val houseNumber: String? = null
) {
    // Unisce città, via e numero civico per fare l'indirizzo completo
    val indirizzoCompleto: String
        get() = when {
            city != null && street != null && houseNumber != null -> "$city, $street, $houseNumber"
            city != null -> city
            else -> "Indirizzo non disponibile"
        }
}