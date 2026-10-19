package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eco_terms")
data class EcoTerm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val term: String,
    val summary: String,
    val meaning: String,
    val example: String,
    val formula: String? = null,
    val module: String,
    val week: Int,
    val category: String,
    val bibliography: String = "Piloña Ortiz / Paul Samuelson",
    val isFavorite: Boolean = false,
    val viewCount: Int = 0,
    val lastViewedTimestamp: Long = 0L,
    val partOfSpeech: String = "Sustantivo",
    val synonyms: String? = null,
    val antonyms: String? = null
)
