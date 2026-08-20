package com.example.beautymap.data.remote.model

import com.google.gson.annotations.SerializedName


data class GeoJsonResponse(          //classe che rappresenta la risposta della chiamata al server
    val features: List<Feature>
)

data class Feature(                  //classe che rappresenta un singolo elemento della risposta
    val id: String? = null,
    val geometry: GeoJsonGeometry,
    val properties: FeatureProperties
)

data class GeoJsonGeometry(       //classe che rappresenta la geometria di un singolo elemento
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
                (coordinates.getOrNull(index) as? Number)?.toDouble() ?: 0.0      //se è null restituisce 0.0
            } else {
                // Se è un Polygon (come la vostra estetista), entriamo dentro le liste annidate
                val primaLista = coordinates.getOrNull(0) as? List<*>      //primo livello di liste del poligono
                val primoPunto = primaLista?.getOrNull(0) as? List<*>      //primo punto appartenente a quella lista
                (primoPunto?.getOrNull(index) as? Number)?.toDouble() ?: 0.0
            }
        } catch (e: Exception) {
            e.printStackTrace()                                                    //stampa l'errore nella console di Android Studio per fare il debug
            0.0
        }
    }
}

data class FeatureProperties(                                                                    //classe che rappresenta le proprietà di un singolo elemento
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
    val soloCitta: String                                                                       //serve per mostrare solo la città nella mappa
        get() = if (!city.isNullOrBlank()) city else "Città non disponibile"

    val soloIndirizzo: String                                                                   //serve per mostrare solo l'indirizzo nella mappa
        get() = when {
            !street.isNullOrBlank() && !houseNumber.isNullOrBlank() -> "$street, $houseNumber"
            !street.isNullOrBlank() -> street
            else -> "Indirizzo non disponibile"
        }

}
