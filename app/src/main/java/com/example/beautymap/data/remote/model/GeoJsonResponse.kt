package com.example.beautymap.data.remote.model

import com.google.gson.annotations.SerializedName


data class GeoJsonResponse(
    val features: List<Feature>
)

data class Feature(
    val id: String? = null,
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
    @SerializedName("name")
    val name: String? = null,

    // Legge sia "email" che "contact:email" (se usate Gson)
    @SerializedName("email", alternate = ["contact:email"])
    val email: String? = null,

    @SerializedName("addr:city", alternate = ["city", "is_in:city"])
    val city: String? = null,

    @SerializedName("addr:street")
    val street: String? = null,

    @SerializedName("addr:housenumber")
    val houseNumber: String? = null,

    @SerializedName("beauty")
    val beauty: String? = null,

    @SerializedName("shop")
    val shop: String? = null,

    @SerializedName("phone", alternate = ["contact:phone", "contact:mobile", "phone:mobile"])
    val phone: String? = null,

    @SerializedName("facebook", alternate = ["contact:facebook"])
    val facebook: String? = null,

    @SerializedName("website", alternate = ["contact:website", "url"])
    val website: String? = null,

    @SerializedName("instagram", alternate = ["contact:instagram"])
    val instagram: String? = null
) {
    val soloCitta: String
        get() = if (!city.isNullOrBlank()) city else "Città non disponibile"

    val soloIndirizzo: String
        get() = when {
            !street.isNullOrBlank() && !houseNumber.isNullOrBlank() -> "$street, $houseNumber"
            !street.isNullOrBlank() -> street
            else -> "Indirizzo non disponibile"
        }

    /*private val indirizzoCompleto: String
        get() {
            val parts = mutableListOf<String>()
            if (!city.isNullOrBlank()) parts.add(city)
            if (!street.isNullOrBlank()) {
                val streetWithNumber = if (!houseNumber.isNullOrBlank()) "$street, $houseNumber" else street
                parts.add(streetWithNumber)
            }
            return if (parts.isNotEmpty()) {
                parts.joinToString(", ")
            } else {
                "Indirizzo non disponibile"
            }
        }*/
}
