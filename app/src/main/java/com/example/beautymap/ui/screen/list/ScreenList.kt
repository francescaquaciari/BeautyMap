package com.example.beautymap.ui.screen.list

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beautymap.domain.model.User
import com.example.beautymap.ui.theme.Pink40
import com.example.beautymap.ui.theme.Purple40
import com.example.beautymap.ui.theme.PurpleGrey40

@Composable
fun ScreenList(
    viewModel: ListViewModel = hiltViewModel(),
    onItemClick: (User) -> Unit = {}
) {
    val uiState = viewModel.uiState
    ListContent(
        items = uiState.items,
        onItemClick = onItemClick
    )
}

@Composable
private fun ListContent(
    items: List<User> = emptyList(),
    onItemClick: (User) -> Unit = {}
) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nessuna estetista trovata",
                style = typography.bodyLarge,
                color = PurpleGrey40
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = items,
            key = { user -> user.name }
        ) { user ->
            ListItem(
                title = user.name,
                subtitle = user.city,
                onItemClick = { onItemClick(user) }
            )
        }
    }
}

@Composable
private fun ListItem(
    title: String = "Nome Estetista",
    subtitle: String = "Città",
    onItemClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Purple40
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typography.titleMedium,
                    color = Purple40
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Pink40,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = subtitle,
                        style = typography.bodyMedium,
                        color = PurpleGrey40
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ListContentPreview() {
    ListContent()
}