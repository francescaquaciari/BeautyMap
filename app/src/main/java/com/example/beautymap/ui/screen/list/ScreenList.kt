package com.example.beautymap.ui.screen.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beautymap.domain.model.User

@Composable    // quello che chiamo dall'esterno che mi fa vedere la lista
fun ScreenList (
    viewModel: ListViewModel = hiltViewModel(), // Inietta il ViewModel creato prima
    onItemClick: (User) -> Unit = {}
){
    val uiState = viewModel.uiState      // Legge lo stato (la lista, se carica, ecc.)
    ListContent(
        items = uiState.items,  // Passa la lista di utenti/estetiste
        onItemClick = onItemClick   // Passa l'azione del click
    )
}
@Composable
private fun ListContent(
    items: List<User> = emptyList(),
    onItemClick: (User) -> Unit = {}    // Cosa succede se clicco su un'estetista?
){
    if (items.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text ("No items")
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        items(items.size){ index ->
            ListItem(
                title = items[index].name,
                subtitle = items[index].city,
                onItemClick = {
                    onItemClick(items[index])
                }
            )
                }
    }
}
@Preview
@Composable
private fun ListItem(
    title: String = "Title",
    subtitle: String = "Subtitle",
    onItemClick: () -> Unit = {}
){
    Column (
        modifier = Modifier.fillMaxWidth().padding(16.dp)   // Margine interno per non toccare i bordi
            .clickable (onClick = onItemClick),     // Rende l'intera riga cliccabile
    ){
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            style = typography.titleMedium      // Stile del font più grande/evident
        )
        Text(
            text = subtitle,
            modifier = Modifier.fillMaxWidth(),
            style = typography.bodyMedium        // Stile del font più piccolo per la città
        )

    }
}

@Preview
@Composable
private fun ListContentPreview(){
    ListContent()
}