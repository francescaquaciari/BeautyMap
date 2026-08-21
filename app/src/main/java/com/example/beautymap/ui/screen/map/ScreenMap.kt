package com.example.beautymap.ui.screen.map

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.beautymap.domain.model.User
import com.example.beautymap.ui.common.PermissionGate
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState


@Composable
fun ScreenMap (
    onItemClick: (User) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState                          //stato della UI

    GoogleMap(                                                //mappa di google a tutto schermo
        modifier = Modifier.fillMaxSize(),
    ) {
        uiState.items.forEach { user ->
            val snippetText = when {                          //sottotitolo della posizione da mostrare sotto il nome del centro
                user.city != "Città non disponibile" && user.address != "Indirizzo non disponibile" -> "${user.city}, ${user.address}"
                user.city != "Città non disponibile" -> user.city
                else -> user.address
            }
            Marker(                                          //singolo marker per ogni centro estetico
                state = rememberUpdatedMarkerState(position = LatLng(user.lat.toDoubleOrNull() ?: 0.0, user.lng.toDoubleOrNull() ?: 0.0)),
                title = user.name,
                snippet = snippetText,
                onInfoWindowClick = {                 //quando si clicca si aprono le info del centro
                    onItemClick(user)
                }
            )
        }

        PermissionGate(                                       //componente che verifica se l'utente ha dato i permessi per il GPS
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current                        //per gestire lo stato della mappa

            DisposableEffect(lifecycleOwner) {         //gestisce l'avvio e la chiusura della schermata
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {                                                        //controlla l'evento di stato
                        Lifecycle.Event.ON_RESUME -> {                                    //quando la schermata è attiva accende la geolocalizzazione
                            @Suppress("MissingPermission")
                            viewModel.onEvent(MapUiEvent.StartLocation)
                        }
                        Lifecycle.Event.ON_PAUSE -> {                                          //quando è in sottofondo spegne la geolocalizzazione
                            @Suppress("MissingPermission")
                            viewModel.onEvent(MapUiEvent.StopLocation)
                        }
                        else -> {}                       //ignora gli altri eventi
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)                       //attiva l'oss sul ciclo di vita
                onDispose {                                                          //rimuove l'oss quando la schermata viene distrutta
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            uiState.location?.let {
                Marker(
                    state = rememberUpdatedMarkerState(it),
                    title = "My location",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }
        }

    }
}