package com.example.beautymap.ui.screen.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beautymap.domain.model.User
import com.example.beautymap.ui.theme.Pink40
import com.example.beautymap.ui.theme.Purple40
import com.example.beautymap.ui.theme.PurpleGrey40

@Composable
fun ScreenList(
    viewModel: ListViewModel = hiltViewModel(),                                            //inietta il list view model usando hilt
    onItemClick: (User) -> Unit = {}
) {
    val uiState = viewModel.uiState                                                            //per lo stato della UI
    ListContent(                                                                               //passa la lista degli utenti a list content
        items = uiState.items,
        onItemClick = onItemClick
    )
}

@Composable
private fun ListContent(                                                               //crea l'interfaccia vera e propria della lista
    items: List<User> = emptyList(),
    onItemClick: (User) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }                         //variabile per memorizzare il testo scritto nella barra di ricerca

    val filteredItems = remember(items, searchQuery) {                   //calcola in automatico gli elementi da mostrare
        if (searchQuery.isBlank()) items                                       //se la ricerca è vuota mostra tutti i centri
        else items.filter { user ->
            user.name.contains(searchQuery, ignoreCase = true) ||
                    user.city.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // --- Header + Barra di Ricerca ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Scopri i centri vicini a te!",
                style = typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Purple40
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(                                                               //barra di ricerca con bordo
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cerca centri estetici o servizi...", color = Color.Gray) },     //testo del campo quando è vuoto
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cerca",
                        tint = Purple40
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Purple40,
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
        }

        // --- Lista Centri ---
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nessun centro estetico trovato",
                    style = typography.bodyLarge,
                    color = PurpleGrey40
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredItems,
                    key = { user -> user.id }
                ) { user ->
                    BeautyCenterCard(
                        user = user,
                        onItemClick = { onItemClick(user) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BeautyCenterCard(                   //singola card per il centro estetico
    user: User,
    onItemClick: () -> Unit = {}
) {
    val locationText = when {
        user.city != "Città non disponibile" && user.address != "Indirizzo non disponibile" -> "${user.city} • ${user.address}"
        user.city != "Città non disponibile" -> user.city
        else -> user.address
    }

    val initialLetter = user.name.trim().take(1).uppercase().ifEmpty { "B" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con Iniziale e Gradiente Viola/Rosa
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Purple40, Pink40)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initialLetter,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Informazioni Principali
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.name,
                        style = typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2C2C2C),
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    // Tag "Estetica"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF3E5F5),
                        modifier = Modifier.padding(start = 6.dp)
                    ) {
                        Text(
                            text = "Estetica",
                            color = Purple40,
                            style = typography.labelSmall,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Purple40,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = locationText,
                        style = typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Freccia di navigazione
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Dettaglio",
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Preview                                        //visualizzare un'anteprima della schermata
@Composable
private fun ListContentPreview() {
    ListContent()
}