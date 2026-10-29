package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.EcoTerm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTermDialog(
    term: EcoTerm,
    onDismiss: () -> Unit,
    onSaveTerm: (EcoTerm) -> Unit
) {
    var termText by remember { mutableStateOf(term.term) }
    var partOfSpeechText by remember { mutableStateOf(term.partOfSpeech) }
    var meaningText by remember { mutableStateOf(term.meaning) }
    var exampleText by remember { mutableStateOf(term.example) }
    var formulaText by remember { mutableStateOf(term.formula ?: "") }
    var synonymsText by remember { mutableStateOf(term.synonyms ?: "") }
    var antonymsText by remember { mutableStateOf(term.antonyms ?: "") }

    var isPartOfSpeechExpanded by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val partsOfSpeech = listOf(
        "Sustantivo",
        "Sustantivo masculino",
        "Sustantivo femenino",
        "Locución sustantiva",
        "Adjetivo",
        "Verbo",
        "Locución adverbial"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "Editar Término",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = termText,
                    onValueChange = { termText = it },
                    label = { Text("Palabra o Término *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_term_name_input"),
                    isError = showError && termText.isBlank()
                )

                // Part of Speech dropdown / input
                ExposedDropdownMenuBox(
                    expanded = isPartOfSpeechExpanded,
                    onExpandedChange = { isPartOfSpeechExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = partOfSpeechText,
                        onValueChange = { partOfSpeechText = it },
                        label = { Text("Tipo de Palabra (Sustantivo, Verbo, etc.)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPartOfSpeechExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isPartOfSpeechExpanded,
                        onDismissRequest = { isPartOfSpeechExpanded = false }
                    ) {
                        partsOfSpeech.forEach { pos ->
                            DropdownMenuItem(
                                text = { Text(pos) },
                                onClick = {
                                    partOfSpeechText = pos
                                    isPartOfSpeechExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = meaningText,
                    onValueChange = { meaningText = it },
                    label = { Text("Significado / Definición *") },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_term_meaning_input"),
                    isError = showError && meaningText.isBlank()
                )

                OutlinedTextField(
                    value = exampleText,
                    onValueChange = { exampleText = it },
                    label = { Text("Ejemplo de Uso Práctico") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = formulaText,
                    onValueChange = { formulaText = it },
                    label = { Text("Fórmula o Expresión (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = synonymsText,
                    onValueChange = { synonymsText = it },
                    label = { Text("Sinónimos (separados por coma)") },
                    placeholder = { Text("Ej. Concepto A, Concepto B") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = antonymsText,
                    onValueChange = { antonymsText = it },
                    label = { Text("Antónimos (separados por coma)") },
                    placeholder = { Text("Ej. Concepto Opuesto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showError && (termText.isBlank() || meaningText.isBlank())) {
                    Text(
                        text = "Por favor completa la palabra y su significado",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (termText.isBlank() || meaningText.isBlank()) {
                        showError = true
                    } else {
                        val trimmedTerm = termText.trim()
                        val initialChar = trimmedTerm.firstOrNull()?.uppercaseChar() ?: 'A'
                        val updatedTerm = term.copy(
                            term = trimmedTerm,
                            partOfSpeech = if (partOfSpeechText.isBlank()) "Sustantivo" else partOfSpeechText.trim(),
                            summary = if (meaningText.length > 90) meaningText.take(87) + "..." else meaningText.trim(),
                            meaning = meaningText.trim(),
                            example = if (exampleText.isBlank()) "Aplicación práctica según la teoría microeconómica." else exampleText.trim(),
                            formula = if (formulaText.isBlank()) null else formulaText.trim(),
                            module = "Glosario General",
                            week = 0,
                            category = "Letra $initialChar",
                            bibliography = term.bibliography.ifBlank { "Guía Didáctica UMG • Microeconomía" },
                            synonyms = if (synonymsText.isBlank()) null else synonymsText.trim(),
                            antonyms = if (antonymsText.isBlank()) null else antonymsText.trim()
                        )
                        onSaveTerm(updatedTerm)
                    }
                },
                modifier = Modifier.testTag("confirm_edit_term_button")
            ) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
