package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioAttributes
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
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
import com.example.ui.screens.StudyFilterType
import java.util.Calendar
import java.util.Locale

enum class AppTab {
    HOME,
    EXPLORE,
    FAVORITES,
    STUDY,
    ABOUT
}

class DictionaryViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository: DictionaryRepository
    private var tts: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)

    private val prefs = application.getSharedPreferences("microdiccionario_prefs", Context.MODE_PRIVATE)
    private val KEY_TUTORIAL_SEEN = "has_seen_tutorial_v1"

    private val _showTutorial = MutableStateFlow(false)
    val showTutorial: StateFlow<Boolean> = _showTutorial.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = DictionaryRepository(database.ecoTermDao())
        initializeNaturalTts(application)

        // Automatically show tutorial only on first installation / launch
        val hasSeenTutorial = prefs.getBoolean(KEY_TUTORIAL_SEEN, false)
        if (!hasSeenTutorial) {
            _showTutorial.value = true
        }
    }

    private fun initializeNaturalTts(application: Application) {
        try {
            // Check available TTS engines to prefer Google Speech Services (WaveNet / Neural voices)
            val testTts = TextToSpeech(application) { /* no-op */ }
            val engines = testTts.engines?.map { it.name } ?: emptyList()
            testTts.shutdown()

            if (engines.contains("com.google.android.tts")) {
                tts = TextToSpeech(application, this, "com.google.android.tts")
            } else {
                tts = TextToSpeech(application, this)
            }
        } catch (_: Exception) {
            tts = TextToSpeech(application, this)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            configureTtsForNaturalLatinFemale()
            _isTtsReady.value = true
        }
    }

    fun configureTtsForLatinFemale() {
        configureTtsForNaturalLatinFemale()
    }

    private fun configureTtsForNaturalLatinFemale() {
        try {
            val latinLocales = listOf(
                Locale("es", "MX"),
                Locale("es", "US"),
                Locale("es", "419"),
                Locale("es", "CO"),
                Locale("es", "GT"),
                Locale("es", "ES"),
                Locale("es")
            )
            for (locale in latinLocales) {
                if ((tts?.isLanguageAvailable(locale) ?: -1) >= TextToSpeech.LANG_AVAILABLE) {
                    tts?.language = locale
                    break
                }
            }

            // Optimize hardware DSP audio pipeline for speech synthesis clarity
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
                .build()
            tts?.setAudioAttributes(audioAttributes)

            // Select highest quality natural/neural voice available
            val allVoices = tts?.voices
            if (!allVoices.isNullOrEmpty()) {
                val availableSpanishVoices = allVoices.filter { v ->
                    v.locale.language == "es" &&
                    !(v.features?.contains(TextToSpeech.Engine.KEY_FEATURE_NOT_INSTALLED) == true)
                }

                if (availableSpanishVoices.isNotEmpty()) {
                    val bestVoice = availableSpanishVoices.maxByOrNull { v ->
                        var score = 0
                        val nameLower = v.name.lowercase(Locale.ROOT)

                        // 1. Prioritize Latin American regions (Mexico, US-LatAm, LatAm-419, Colombia, Guatemala)
                        if (v.locale.country in listOf("MX", "US", "419", "CO", "GT")) score += 60

                        // 2. High-quality tier from Android Voice API
                        if (v.quality == Voice.QUALITY_VERY_HIGH) score += 50
                        else if (v.quality == Voice.QUALITY_HIGH) score += 30

                        // 3. Neural, WaveNet, or natural model names
                        if (nameLower.contains("wavenet") || nameLower.contains("neural") || nameLower.contains("natural")) score += 45
                        if (nameLower.contains("network")) score += 35 // Network-rendered voices have human-level prosody

                        // 4. Female voice identifiers in Google TTS & Android engines
                        if (nameLower.contains("female") || nameLower.contains("#female") ||
                            nameLower.contains("-sfb") || nameLower.contains("-sfa") ||
                            nameLower.contains("-sfd") || nameLower.contains("-sfg") ||
                            nameLower.contains("mujer") || nameLower.contains("-eed")) {
                            score += 65
                        }

                        // 5. Heavily penalize male identifiers
                        if ((nameLower.contains("male") && !nameLower.contains("female")) ||
                            nameLower.contains("#male") || nameLower.contains("-sfc") || nameLower.contains("-sfe")) {
                            score -= 80
                        }

                        score
                    }

                    if (bestVoice != null) {
                        tts?.voice = bestVoice
                    }
                }
            }

            // NATURAL HUMAN VOICE CONFIGURATION:
            // Pitch 1.0f preserves the natural harmonic formants and resonance of human speech.
            // (Previous pitch was artificially forced to 1.22f, creating metallic/robotic pitch-shift artifacts).
            tts?.setPitch(1.0f)

            // Speech rate 0.92f gives warm, clear, unhurried, natural articulation
            tts?.setSpeechRate(0.92f)
        } catch (_: Exception) {
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(0.92f)
        }
    }

    val allTerms: StateFlow<List<EcoTerm>> = repository.allTerms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteTerms: StateFlow<List<EcoTerm>> = repository.favoriteTerms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

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

    // Filtered terms based on search query with smart prefix-first search
    val filteredTerms: StateFlow<List<EcoTerm>> = combine(
        allTerms,
        _searchQuery
    ) { terms, query ->
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            terms.sortedBy { it.term.lowercase(Locale.ROOT) }
        } else if (trimmed.length == 1) {
            // When a single letter is entered: ONLY return terms starting with that letter
            terms.filter { term ->
                term.term.trim().startsWith(trimmed, ignoreCase = true)
            }.sortedBy { it.term.lowercase(Locale.ROOT) }
        } else {
            // As user types:
            // 1. Terms whose name starts with the query (e.g. "Cost" -> "Costo", "Costo marginal")
            // 2. Terms where any word in the name starts with the query (e.g. "marginal" -> "Costo marginal")
            // 3. Terms whose name contains the query anywhere
            val exactPrefixMatches = mutableListOf<EcoTerm>()
            val wordPrefixMatches = mutableListOf<EcoTerm>()
            val containsMatches = mutableListOf<EcoTerm>()
            val seenIds = mutableSetOf<Int>()

            for (term in terms) {
                val name = term.term.trim()
                if (name.startsWith(trimmed, ignoreCase = true)) {
                    exactPrefixMatches.add(term)
                    seenIds.add(term.id)
                }
            }

            for (term in terms) {
                if (term.id !in seenIds) {
                    val words = term.term.trim().split("\\s+".toRegex())
                    if (words.any { it.startsWith(trimmed, ignoreCase = true) }) {
                        wordPrefixMatches.add(term)
                        seenIds.add(term.id)
                    }
                }
            }

            for (term in terms) {
                if (term.id !in seenIds) {
                    if (term.term.contains(trimmed, ignoreCase = true)) {
                        containsMatches.add(term)
                        seenIds.add(term.id)
                    }
                }
            }

            exactPrefixMatches.sortedBy { it.term.lowercase(Locale.ROOT) } +
            wordPrefixMatches.sortedBy { it.term.lowercase(Locale.ROOT) } +
            containsMatches.sortedBy { it.term.lowercase(Locale.ROOT) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected term detail
    val selectedTerm: StateFlow<EcoTerm?> = _selectedTermId.flatMapLatest { id ->
        if (id == null) MutableStateFlow(null)
        else repository.getTermById(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Study Flashcard Mode State
    private val _studyFilter = MutableStateFlow(StudyFilterType.ALL)
    val studyFilter: StateFlow<StudyFilterType> = _studyFilter.asStateFlow()

    private val _studyCardIndex = MutableStateFlow(0)
    val studyCardIndex: StateFlow<Int> = _studyCardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    fun setStudyFilter(filter: StudyFilterType) {
        _studyFilter.value = filter
    }

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

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
        if (tab != AppTab.STUDY) {
            _studyFilter.value = StudyFilterType.ALL
        }
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

    fun addNewTerm(term: EcoTerm) {
        viewModelScope.launch {
            repository.addTerm(term)
        }
    }

    fun updateTerm(term: EcoTerm) {
        viewModelScope.launch {
            repository.updateTerm(term)
        }
    }

    fun deleteTerm(term: EcoTerm) {
        viewModelScope.launch {
            repository.deleteTerm(term)
            if (_selectedTermId.value == term.id) {
                _selectedTermId.value = null
            }
        }
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
            configureTtsForNaturalLatinFemale()
            val textToSpeak = "${term.term}. ${term.meaning}. Ejemplo: ${term.example}"
            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, "EcoTTS_${System.currentTimeMillis()}")
        }
    }

    // Pronounce ONLY the word with natural Latin American female voice
    fun speakTermPronunciation(term: EcoTerm) {
        if (_isTtsReady.value) {
            configureTtsForNaturalLatinFemale()
            val params = Bundle().apply {
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            tts?.speak(term.term, TextToSpeech.QUEUE_FLUSH, params, "EcoWordPronounceTTS_${System.currentTimeMillis()}")
        }
    }

    fun stopTts() {
        tts?.stop()
    }

    fun openTutorial() {
        _showTutorial.value = true
    }

    fun completeTutorial() {
        _showTutorial.value = false
        prefs.edit().putBoolean(KEY_TUTORIAL_SEEN, true).apply()
    }

    fun dismissTutorial() {
        _showTutorial.value = false
        prefs.edit().putBoolean(KEY_TUTORIAL_SEEN, true).apply()
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}
