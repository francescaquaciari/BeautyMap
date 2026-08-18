package com.example.beautymap.ui.screen.map

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

sealed class MapUiEvent {
    data object StartLocation: MapUiEvent()
    data object StopLocation: MapUiEvent()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val locationHelper: LocationHelper
    ) : ViewModel() {

    var uiState by mutableStateOf(MapUiState())
        private set

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return

            uiState = uiState.copy(location = LatLng(location.latitude, location.longitude))
        }
    }

    init {
        load()
    }

    fun onEvent (event: MapUiEvent){
        when(event){
            is MapUiEvent.StartLocation -> locationHelper.start(locationCallback)
            is MapUiEvent.StopLocation -> locationHelper.stop(locationCallback)
        }
    }

    private fun load() {
        viewModelScope.launch {
            getUsersUseCase().collect {
                uiState = when(it) {
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = it.data, isLoading = false)
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)    // 01:24:00


                }
            }
        }

    }
}