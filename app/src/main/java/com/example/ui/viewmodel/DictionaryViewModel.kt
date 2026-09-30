package com.example.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.EcoTerm
import com.example.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

enum class AppTab {
    HOME,
    EXPLORE,
    FAVORITES,
    STUDY
}

class DictionaryViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository: DictionaryRepository
    private var tts: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = DictionaryRepository(database.ecoTermDao())
        tts = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                _isTtsReady.value = true
            }
        }
    }

    val allTerms: StateFlow<List<EcoTerm>> = repository.allTerms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteTerms: StateFlow<List<EcoTerm>> = repository.favoriteTerms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedModule = MutableStateFlow("Todos")
    val selectedModule: StateFlow<String> = _selectedModule.asStateFlow()

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedTermId = MutableStateFlow<Int?>(null)
    val selectedTermId: StateFlow<Int?> = _selectedTermId.asStateFlow()

    private val _randomWordOffset = MutableStateFlow(0)

    // Word of the Day derived deterministically by calendar day + optional offset
    val wordOfTheDay: StateFlow<EcoTerm?> = combine(allTerms, _randomWordOffset) { terms, offset ->
        if (terms.isEmpty()) null
        else {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val index = (dayOfYear + offset) % terms.size
            terms[index.coerceAtLeast(0)]
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Filtered terms based on query and module selection
    val filteredTerms: StateFlow<List<EcoTerm>> = combine(
        allTerms,
        _searchQuery,
        _selectedModule
    ) { terms, query, module ->
        terms.filter { term ->
            val matchesQuery = if (query.isBlank()) true else {
                term.term.contains(query, ignoreCase = true) ||
                term.summary.contains(query, ignoreCase = true) ||
                term.meaning.contains(query, ignoreCase = true) ||
                term.category.contains(query, ignoreCase = true)
            }
            val matchesModule = if (module == "Todos") true else {
                term.module.startsWith(module) || term.module.contains(module)
            }
            matchesQuery && matchesModule
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected term detail
    val selectedTerm: StateFlow<EcoTerm?> = _selectedTermId.flatMapLatest { id ->
        if (id == null) MutableStateFlow(null)
        else repository.getTermById(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Study Flashcard Mode State
    private val _studyCardIndex = MutableStateFlow(0)
    val studyCardIndex: StateFlow<Int> = _studyCardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onVoiceSearchInput(voiceText: String) {
        _searchQuery.value = voiceText.trim()
        // If user is on HOME and searches, switch to EXPLORE or show results
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun setModuleFilter(module: String) {
        _selectedModule.value = module
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
        // Clear term detail when navigating tabs
        _selectedTermId.value = null
    }

    fun openTermDetail(termId: Int) {
        _selectedTermId.value = termId
        viewModelScope.launch {
            repository.recordView(termId)
        }
    }

    fun closeTermDetail() {
        _selectedTermId.value = null
        stopTts()
    }

    fun toggleFavorite(term: EcoTerm) {
        viewModelScope.launch {
            repository.toggleFavorite(term)
        }
    }

    fun nextRandomWord() {
        _randomWordOffset.value = _randomWordOffset.value + 1
    }

    // Flashcards Navigation
    fun nextStudyCard(total: Int) {
        if (total > 0) {
            _studyCardIndex.value = (_studyCardIndex.value + 1) % total
            _isCardFlipped.value = false
        }
    }

    fun prevStudyCard(total: Int) {
        if (total > 0) {
            _studyCardIndex.value = if (_studyCardIndex.value - 1 < 0) total - 1 else _studyCardIndex.value - 1
            _isCardFlipped.value = false
        }
    }

    fun flipCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    // Text to Speech
    fun speakTerm(term: EcoTerm) {
        if (_isTtsReady.value) {
            val textToSpeak = "${term.term}. ${term.meaning}. Ejemplo: ${term.example}"
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "EcoTTS")
        }
    }

    fun stopTts() {
        tts?.stop()
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}
