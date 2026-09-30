package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.EcoTerm
import com.example.ui.components.ModuleFilterChips
import com.example.ui.components.SearchBarWithMic
import com.example.ui.components.TermCard

@Composable
fun ExploreScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMicClick: () -> Unit,
    onClearSearch: () -> Unit,
    selectedModule: String,
    onSelectModule: (String) -> Unit,
    terms: List<EcoTerm>,
    onSelectTerm: (Int) -> Unit,
    onToggleFavorite: (EcoTerm) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("explore_screen_content")
    ) {
        // Sticky Header with Search and Module Filter
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SearchBarWithMic(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onMicClick = onMicClick,
                onClearClick = onClearSearch,
                placeholderText = "Buscar en todo el diccionario..."
            )

            ModuleFilterChips(
                selectedModule = selectedModule,
                onModuleSelect = onSelectModule
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${terms.size} términos disponibles",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (selectedModule != "Todos") {
                    Text(
                        text = "Filtrando por $selectedModule",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }

        // List of Terms
        if (terms.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No se encontraron términos para esta búsqueda",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = {
                    onClearSearch()
                    onSelectModule("Todos")
                }) {
                    Text("Restablecer filtros")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(terms, key = { it.id }) { term ->
                    TermCard(
                        term = term,
                        onClick = { onSelectTerm(term.id) },
                        onFavoriteToggle = { onToggleFavorite(term) }
                    )
                }
            }
        }
    }
}
