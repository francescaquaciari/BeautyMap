package com.example.beautymap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.beautymap.domain.model.User
import com.example.beautymap.ui.screen.detail.ScreenDetail
import com.example.beautymap.ui.screen.list.ScreenList
import com.example.beautymap.ui.screen.map.ScreenMap
import com.example.beautymap.ui.theme.BeautyMapTheme
import dagger.hilt.android.AndroidEntryPoint

data object ListScreen      //identificatore schermata lista
data object MapScreen        //identificatore schermata mappa
data class DetailScreen (val user: User)              //schermata di dettaglio e porta co sè i dati dell'estetista

@AndroidEntryPoint                                                              //dice a Hilt che è il punto di ingresso principale
class MainActivity : ComponentActivity() {                                       //eredita le funzionalità base delle activity di Android
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)                                      //inizializzazione standard del sistema di android
        enableEdgeToEdge()                                                      // Fa sì che l'app usi tutto lo schermo, fin sotto la barra di stato
        setContent {                                                            // Qui inizia il disegno dell'interfaccia
            BeautyMapTheme {
                val backStack = remember { mutableStateListOf<Any>(ListScreen) }    //crea e memorizza lo storico delle schermate

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar() {
                            NavigationBarItem(
                                selected = backStack.lastOrNull() is ListScreen,
                                onClick = {
                                    if (backStack.lastOrNull() !is ListScreen)
                                        backStack.add(ListScreen)                              //aggiunge la lista in cima allo storico e la mostra a schermo

                                },
                                icon = {
                                    Icon (Icons.Default.Home, contentDescription = "List")
                                },
                                label = { Text(text = "List") }
                            )

                            NavigationBarItem(
                                selected = backStack.lastOrNull() is MapScreen,                   //se la schermata aperta è la mappa illumina il pulsante
                                onClick = {
                                    if (backStack.lastOrNull() !is MapScreen) {
                                        backStack.add(MapScreen)
                                    }
                                },
                                icon = {
                                    Icon (Icons.Default.Place, contentDescription = "Map")
                                },
                                label = { Text(text = "Map") }



                            )
                        }
                    }
                    ) { innerPadding ->
                    NavDisplay(                                                               //crea lo storico delle schermate
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull()},                             //rimuove l'ultima schermata dallo storico e torna alla precedente
                        entryProvider = entryProvider {             //definisce quale schermata mostrare

                            entry<ListScreen> {
                                ScreenList (
                                    onItemClick = {
                                        backStack.add(DetailScreen(it))
                                    }
                                )
                            }
                            entry<MapScreen> {
                                ScreenMap (
                                    onItemClick = {
                                        backStack.add(DetailScreen(it))
                                    }
                                )


                            }
                            entry<DetailScreen> {
                                val user = it.user
                                ScreenDetail(user)

                            }
                        }
                    )
                }
            }
        }
    }
}
