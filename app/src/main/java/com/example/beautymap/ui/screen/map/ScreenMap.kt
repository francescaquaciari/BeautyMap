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
import androidx.lifecycle.compose.currentStateAsState


@Composable
fun ScreenMap (
    onItemClick: (User) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
    ) {
        uiState.items.forEach { user ->
            val snippetText = when {
                user.city != "Città non disponibile" && user.address != "Indirizzo non disponibile" -> "${user.city}, ${user.address}"
                user.city != "Città non disponibile" -> user.city
                else -> user.address
            }
            Marker(
                state = rememberUpdatedMarkerState(position = LatLng(user.lat.toDoubleOrNull() ?: 0.0, user.lng.toDoubleOrNull() ?: 0.0)),
                title = user.name,
                snippet = snippetText,
                onInfoWindowClick = {
                    onItemClick(user)
                }
            )
        }

        PermissionGate(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {

            val localLifecycle = LocalLifecycleOwner.current
            DisposableEffect(localLifecycle.lifecycle.currentStateAsState().value) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_RESUME -> viewModel.onEvent(MapUiEvent.StartLocation)
                        Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(MapUiEvent.StopLocation)
                        else -> {}
                    }
                }
                localLifecycle.lifecycle.addObserver(observer)
                onDispose {
                    localLifecycle.lifecycle.removeObserver(observer)
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