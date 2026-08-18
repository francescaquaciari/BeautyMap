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

data object ListScreen
data object MapScren
data class DetailScreen (val user: User)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Fa sì che l'app usi tutto lo schermo, fin sotto la barra di stato
        setContent {  // Qui inizia il disegno dell'interfaccia
            BeautyMapTheme {  // Applica i colori e i font del vostro tema
                val backStack = remember { mutableStateListOf<Any>(ListScreen) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar() {
                            NavigationBarItem(
                                selected = backStack.lastOrNull() is ListScreen,
                                onClick = {
                                    if (backStack.lastOrNull() !is ListScreen)
                                        backStack.add(ListScreen)

                                },
                                icon = {
                                    Icon (Icons.Default.Home, contentDescription = "List")        // possiamo cambiare icona DA CAMBIARE!
                                },
                                label = { Text(text = "List") }
                            )

                            NavigationBarItem(
                                selected = backStack.lastOrNull() is MapScren,
                                onClick = {
                                    if (backStack.lastOrNull() !is MapScren) {
                                        backStack.add(MapScren)
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
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull()},
                        entryProvider = entryProvider {

                            entry<ListScreen> {
                                ScreenList (
                                    onItemClick = {
                                        backStack.add(DetailScreen(it))
                                    }
                                )
                            }
                            entry<MapScren> {
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
