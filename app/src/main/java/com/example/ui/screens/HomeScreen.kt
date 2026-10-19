package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.R
import com.example.data.model.EcoTerm
import com.example.ui.components.AddTermDialog
import com.example.ui.components.KebabFloatingMenu
import com.example.ui.components.SearchBarWithMic
import com.example.ui.components.TermCard
import com.example.ui.components.WordOfTheDayCard

@Composable
fun HomeScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onMicClick: () -> Unit,
    onClearSearch: () -> Unit,
    wordOfTheDay: EcoTerm?,
    filteredTerms: List<EcoTerm>,
    favoriteCount: Int,
    totalTermsCount: Int,
    onSelectTerm: (Int) -> Unit,
    onShuffleWordOfTheDay: () -> Unit,
    onToggleFavorite: (EcoTerm) -> Unit,
    onExploreAllClick: () -> Unit,
    onOpenFavoritesClick: () -> Unit,
    onOpenStudyClick: () -> Unit,
    onAddNewTerm: (EcoTerm) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isSearching = searchQuery.trim().isNotEmpty()
    var showAddDialog by remember { mutableStateOf(false) }
    var isKebabExpanded by remember { mutableStateOf(false) }

    // Dismiss kebab menu on back button press
    BackHandler(enabled = isKebabExpanded) {
        isKebabExpanded = false
    }

    if (showAddDialog) {
        AddTermDialog(
            onDismiss = { showAddDialog = false },
            onSaveTerm = { newTerm ->
                onAddNewTerm(newTerm)
                showAddDialog = false
                isKebabExpanded = false
                Toast.makeText(context, "¡Palabra \"${newTerm.term}\" agregada!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .testTag("home_screen_content"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Header Banner (Persistent across search state)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("umg_header_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "MicroDiccionario",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Guía Didáctica UMG",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar with Voice Mic button (Persistent across search state - NEVER unmounted)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Búsqueda Rápida",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                SearchBarWithMic(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onMicClick = onMicClick,
                    onClearClick = onClearSearch,
                    placeholderText = "Escribe o usa el micrófono para buscar..."
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dynamic section below the search bar
            if (isSearching) {
                // Search results list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("home_search_results_content")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Resultados (${filteredTerms.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Toca para ver detalle",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (filteredTerms.isEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No se encontraron coincidencias para \"$searchQuery\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = onClearSearch,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text("Limpiar búsqueda")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredTerms, key = { it.id }) { term ->
                                TermCard(
                                    term = term,
                                    onClick = { onSelectTerm(term.id) },
                                    onFavoriteToggle = { onToggleFavorite(term) }
                                )
                            }
                        }
                    }
                }
            } else {
                // Default Home Content (Logo, Word of the Day, etc.)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Official UMG Shield Logo positioned centrally right above Palabra del Día
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_umg),
                            contentDescription = "Escudo Universidad Mariano Gálvez de Guatemala",
                            modifier = Modifier
                                .size(200.dp)
                                .testTag("umg_shield_logo"),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Box holding Palabra del Día and Kebab button superimposed
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        WordOfTheDayCard(
                            term = wordOfTheDay,
                            onCardClick = {
                                if (!isKebabExpanded) {
                                    wordOfTheDay?.let { onSelectTerm(it.id) }
                                }
                            },
                            onShuffleClick = onShuffleWordOfTheDay,
                            onFavoriteToggle = {
                                wordOfTheDay?.let { onToggleFavorite(it) }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Floating Kebab Anchor Area (Bottom Right)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 4.dp, bottom = 10.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        // Empty spacer reserving height so layout doesn't jump
                        Spacer(modifier = Modifier.size(54.dp))
                    }
                }
            }
        }

        // Dark Backdrop Scrim Overlay (dims background to highlight kebab options)
        AnimatedVisibility(
            visible = isKebabExpanded && !isSearching,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.58f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        isKebabExpanded = false
                    }
                    .testTag("kebab_backdrop_scrim")
            )
        }

        // Kebab Menu Superimposed on top of the darkened background and over the lower right of the card
        if (!isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .zIndex(3f),
                contentAlignment = Alignment.BottomEnd
            ) {
                KebabFloatingMenu(
                    isExpanded = isKebabExpanded,
                    onToggleExpanded = { isKebabExpanded = !isKebabExpanded },
                    favoriteCount = favoriteCount,
                    onOpenFavorites = {
                        isKebabExpanded = false
                        onOpenFavoritesClick()
                    },
                    onOpenAddWord = {
                        isKebabExpanded = false
                        showAddDialog = true
                    },
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }
}
