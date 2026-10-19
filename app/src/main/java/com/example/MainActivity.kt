package com.example

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StudyFlashcardsScreen
import com.example.ui.screens.StudyFilterType
import com.example.ui.screens.WordDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.DictionaryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DictionaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                DictionaryApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DictionaryApp(viewModel: DictionaryViewModel) {
    val context = LocalContext.current

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedTerm by viewModel.selectedTerm.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredTerms by viewModel.filteredTerms.collectAsStateWithLifecycle()
    val allTerms by viewModel.allTerms.collectAsStateWithLifecycle()
    val favoriteTerms by viewModel.favoriteTerms.collectAsStateWithLifecycle()
    val wordOfTheDay by viewModel.wordOfTheDay.collectAsStateWithLifecycle()
    val studyFilter by viewModel.studyFilter.collectAsStateWithLifecycle()

    // Android Speech Recognition Launcher
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.onVoiceSearchInput(spokenText)
                Toast.makeText(context, "Buscando: \"$spokenText\"", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val launchVoiceSearch: () -> Unit = {
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    "Di el término económico a buscar (ej. TMS, Curva de Engel, Monopolio)..."
                )
            }
            speechLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "El reconocimiento de voz no está disponible en este dispositivo",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // If a term detail is open, show WordDetailScreen
    if (selectedTerm != null) {
        WordDetailScreen(
            term = selectedTerm,
            onBack = { viewModel.closeTermDetail() },
            onToggleFavorite = { viewModel.toggleFavorite(it) },
            onPronounceWord = { viewModel.speakTermPronunciation(it) },
            onUpdateTerm = { viewModel.updateTerm(it) },
            onDeleteTerm = { viewModel.deleteTerm(it) }
        )
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag("app_main_scaffold"),
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.testTag("app_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppTab.HOME,
                        onClick = { viewModel.setTab(AppTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Inicio"
                            )
                        },
                        label = { Text("Inicio") },
                        modifier = Modifier.testTag("nav_tab_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.EXPLORE,
                        onClick = { viewModel.setTab(AppTab.EXPLORE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.EXPLORE) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                contentDescription = "Explorar"
                            )
                        },
                        label = { Text("Explorar") },
                        modifier = Modifier.testTag("nav_tab_explore")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.STUDY,
                        onClick = { viewModel.setTab(AppTab.STUDY) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.STUDY) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                                contentDescription = "Repaso"
                            )
                        },
                        label = { Text("Repaso") },
                        modifier = Modifier.testTag("nav_tab_study")
                    )

                    NavigationBarItem(
                        selected = currentTab == AppTab.ABOUT,
                        onClick = { viewModel.setTab(AppTab.ABOUT) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppTab.ABOUT) Icons.Filled.Info else Icons.Outlined.Info,
                                contentDescription = "Acerca"
                            )
                        },
                        label = { Text("Acerca") },
                        modifier = Modifier.testTag("nav_tab_about")
                    )
                }
            }
        ) { innerPadding ->
            when (currentTab) {
                AppTab.HOME -> {
                    HomeScreen(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onMicClick = launchVoiceSearch,
                        onClearSearch = { viewModel.clearSearch() },
                        wordOfTheDay = wordOfTheDay,
                        filteredTerms = filteredTerms,
                        favoriteCount = favoriteTerms.size,
                        totalTermsCount = allTerms.size,
                        onSelectTerm = { viewModel.openTermDetail(it) },
                        onShuffleWordOfTheDay = { viewModel.nextRandomWord() },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onExploreAllClick = { viewModel.setTab(AppTab.EXPLORE) },
                        onOpenFavoritesClick = { viewModel.setTab(AppTab.FAVORITES) },
                        onOpenStudyClick = { viewModel.setTab(AppTab.STUDY) },
                        onAddNewTerm = { viewModel.addNewTerm(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                AppTab.EXPLORE -> {
                    ExploreScreen(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onMicClick = launchVoiceSearch,
                        onClearSearch = { viewModel.clearSearch() },
                        terms = filteredTerms,
                        onSelectTerm = { viewModel.openTermDetail(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                AppTab.FAVORITES -> {
                    FavoritesScreen(
                        favoriteTerms = favoriteTerms,
                        onSelectTerm = { viewModel.openTermDetail(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onStartStudy = {
                            viewModel.setStudyFilter(StudyFilterType.FAVORITES)
                            viewModel.setTab(AppTab.STUDY)
                        },
                        onExploreTerms = { viewModel.setTab(AppTab.EXPLORE) },
                        onBack = { viewModel.setTab(AppTab.HOME) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                AppTab.STUDY -> {
                    StudyFlashcardsScreen(
                        allTerms = allTerms,
                        favoriteTerms = favoriteTerms,
                        initialFilter = studyFilter,
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onSpeakTerm = { viewModel.speakTerm(it) },
                        onBack = { viewModel.setTab(AppTab.HOME) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                AppTab.ABOUT -> {
                    AboutScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
