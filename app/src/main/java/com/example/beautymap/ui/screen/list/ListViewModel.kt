package com.example.beautymap.ui.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautymap.domain.model.User
import com.example.beautymap.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.beautymap.common.Result
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class ListUiState(                                        //dati con le info che dovranno essere mostrate nella grafica
    val items: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
@HiltViewModel                                                    //segnala a hilt che è un VIewModel
class ListViewModel @Inject constructor(                          //inietta i parametri del costruttore
    private val getUsersUseCase: GetUsersUseCase
    ) : ViewModel() {                                              //eredita dalla classe ViewModel di Android
    var uiState by mutableStateOf(ListUiState())            //variabile che contiene lo stato della UI
        private set
    init {
        load()
    }
    private fun load() {                                             //fun privata per scaricare la lista
        viewModelScope.launch {                    //avvia la fun in background
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