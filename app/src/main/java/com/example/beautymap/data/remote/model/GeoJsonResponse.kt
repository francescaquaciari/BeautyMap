package com.example.beautymap.data.remote.model

import com.google.gson.annotations.SerializedName


data class GeoJsonResponse(
    val features: List<Feature>
)

data class Feature(
    val geometry: GeoJsonGeometry,
    val properties: FeatureProperties
)

data class GeoJsonGeometry(
    val type: String?,
    val coordinates: List<Any>
) {
    // Estrae in sicurezza la longitudine, sia che sia un punto singolo sia che sia un poligono
    val longitude: Double
        get() = estraiCoordinata(0)

    // Estrae in sicurezza la latitudine, sia che sia un punto singolo sia che sia un poligono
    val latitude: Double
        get() = estraiCoordinata(1)

    private fun estraiCoordinata(index: Int): Double {
        return try {
            if (type == "Point") {
                // Se è un punto singolo, le coordinate sono [lng, lat]
                (coordinates.getOrNull(index) as? Number)?.toDouble() ?: 0.0
            } else {
                // Se è un Polygon (come la vostra estetista), entriamo dentro le liste annidate
                val primaLista = coordinates.getOrNull(0) as? List<*>
                val primoPunto = primaLista?.getOrNull(0) as? List<*>
                (primoPunto?.getOrNull(index) as? Number)?.toDouble() ?: 0.0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }
}

data class FeatureProperties(
    @SerializedName("name") val name: String? = "Centro Estetico",
    @SerializedName("beauty") val beauty: String? = null,
    @SerializedName("shop") val shop: String? = null,
    @SerializedName("addr:street") val street: String? = null,
    @SerializedName("addr:housenumber") val houseNumber: String? = null
) {
    // Unisce via e numero civico se presenti, altrimenti usa i tag del negozio
    val indirizzoCompleto: String
        get() = when {
            street != null && houseNumber != null -> "$street, $houseNumber"
            street != null -> street
            shop != null && beauty != null -> "$shop ($beauty)"
            shop != null -> shop
            else -> "Indirizzo non disponibile"
        }
}