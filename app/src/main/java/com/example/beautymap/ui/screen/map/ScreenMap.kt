package com.example.beautymap.ui.screen.map

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.beautymap.domain.model.User
import com.example.beautymap.ui.common.PermissionGate
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun ScreenMap (
    onItemClick: (User) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    // Posizione iniziale di default (es. Italia/Roma)
    val defaultLocation = LatLng(41.9028, 12.4964)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 6f)
    }

    // Centra la mappa sulla posizione dell'utente o sul primo centro caricato
    LaunchedEffect(uiState.location, uiState.items) {
        if (uiState.location != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(uiState.location, 12f)
        } else if (uiState.items.isNotEmpty()) {
            val firstUser = uiState.items.firstOrNull {
                it.lat.toDoubleOrNull() != null && it.lng.toDoubleOrNull() != null
            }
            if (firstUser != null) {
                val lat = firstUser.lat.toDoubleOrNull() ?: 41.9028
                val lng = firstUser.lng.toDoubleOrNull() ?: 12.4964
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(lat, lng), 10f)
            }
        }
    }

    // Gestione dei permessi di geolocalizzazione fuori dal blocco GoogleMap
    PermissionGate(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        @Suppress("MissingPermission")
                        viewModel.onEvent(MapUiEvent.StartLocation)
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        @Suppress("MissingPermission")
                        viewModel.onEvent(MapUiEvent.StopLocation)
                    }
                    else -> {}
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Rendering dei punti di interesse (estetiste)
            uiState.items.forEach { user ->
                val lat = user.lat.toDoubleOrNull()
                val lng = user.lng.toDoubleOrNull()
                if (lat != null && lng != null && (lat != 0.0 || lng != 0.0)) {
                    val snippetText = when {
                        user.city != "Città non disponibile" && user.address != "Indirizzo non disponibile" -> "${user.city}, ${user.address}"
                        user.city != "Città non disponibile" -> user.city
                        else -> user.address
                    }
                    Marker(
                        state = rememberUpdatedMarkerState(position = LatLng(lat, lng)),
                        title = user.name,
                        snippet = snippetText,
                        onInfoWindowClick = {
                            onItemClick(user)
                        }
                    )
                }
            }

            // Marker per la posizione corrente dell'utente
            uiState.location?.let {
                Marker(
                    state = rememberUpdatedMarkerState(it),
                    title = "La mia posizione",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }
        }
    }
}