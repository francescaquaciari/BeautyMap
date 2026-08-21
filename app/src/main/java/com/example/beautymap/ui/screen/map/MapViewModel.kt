package com.example.beautymap.ui.screen.map

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautymap.common.LocationHelper
import com.example.beautymap.domain.model.User
import com.example.beautymap.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.beautymap.common.Result
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class MapUiState(
    val items: List<User> = emptyList(),    // Le estetiste da mostrare sulla mappa
    val isLoading: Boolean = false, // Se sta caricando i dati dal server
    val error: String? = null,  // Messaggio di errore
    val location: LatLng? = null    // Le posizione GPS attuale dell'utente (latitudine e longitudine)
)

sealed class MapUiEvent {                                    //definisce il gruppo chiuso di azioni che la mappa invia al view model
    data object StartLocation: MapUiEvent()                   //avvio tracciamento GPS
    data object StopLocation: MapUiEvent()                    //stop tracciamento GPS
}

@HiltViewModel
class MapViewModel @Inject constructor(                        //iniezione automatica della classe di helper di localizzazione
    private val getUsersUseCase: GetUsersUseCase,
    private val locationHelper: LocationHelper
    ) : ViewModel() {

    var uiState by mutableStateOf(MapUiState())                          //stato della UI
        private set

    private val locationCallback = object : LocationCallback() {                 //ricevitore ogni volta che viene inviata una nuova posizione
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return

            uiState = uiState.copy(location = LatLng(location.latitude, location.longitude))
        }
    }

    init {
        load()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])     //sono necessari i permessi
    fun onEvent (event: MapUiEvent){
        when(event){
            is MapUiEvent.StartLocation -> locationHelper.start(locationCallback)
            is MapUiEvent.StopLocation -> locationHelper.stop(locationCallback)
        }
    }

    private fun load() {
        viewModelScope.launch {                                //lavora in background
            getUsersUseCase().collect {
                uiState = when(it) {
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = it.data, isLoading = false)
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)


                }
            }
        }

    }
}