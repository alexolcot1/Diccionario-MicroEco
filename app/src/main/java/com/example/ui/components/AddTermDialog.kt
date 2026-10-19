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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
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
fun AddTermDialog(
    onDismiss: () -> Unit,
    onSaveTerm: (EcoTerm) -> Unit
) {
    var termText by remember { mutableStateOf("") }
    var partOfSpeechText by remember { mutableStateOf("Sustantivo") }
    var meaningText by remember { mutableStateOf("") }
    var exampleText by remember { mutableStateOf("") }
    var formulaText by remember { mutableStateOf("") }
    var synonymsText by remember { mutableStateOf("") }
    var antonymsText by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf("Conceptos Fundamentales") }
    var selectedModule by remember { mutableStateOf("Módulo I: Introducción") }
    var weekNumber by remember { mutableStateOf("1") }
    var isModuleExpanded by remember { mutableStateOf(false) }
    var isPartOfSpeechExpanded by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }

    val modules = listOf(
        "Módulo I: Introducción",
        "Módulo II: Tasas, Oferta y Demanda",
        "Módulo III: Elasticidad y Mercado"
    )

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
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    text = "Agregar Nueva Palabra",
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
                    label = { Text("Palabra o Término Económico *") },
                    placeholder = { Text("Ej. Costo Marginal") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_term"),
                    isError = showError && termText.isBlank()
                )

                // Part of Speech dropdown
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

                // Module selector
                ExposedDropdownMenuBox(
                    expanded = isModuleExpanded,
                    onExpandedChange = { isModuleExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedModule,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Módulo de la Guía") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isModuleExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isModuleExpanded,
                        onDismissRequest = { isModuleExpanded = false }
                    ) {
                        modules.forEach { mod ->
                            DropdownMenuItem(
                                text = { Text(mod) },
                                onClick = {
                                    selectedModule = mod
                                    isModuleExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = categoryText,
                        onValueChange = { categoryText = it },
                        label = { Text("Categoría") },
                        placeholder = { Text("Ej. Producción") },
                        singleLine = true,
                        modifier = Modifier.weight(1.5f)
                    )

                    OutlinedTextField(
                        value = weekNumber,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 2) {
                                weekNumber = it
                            }
                        },
                        label = { Text("Semana") },
                        placeholder = { Text("1-17") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = meaningText,
                    onValueChange = { meaningText = it },
                    label = { Text("Significado / Definición *") },
                    placeholder = { Text("Explica detalladamente el concepto...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_new_meaning"),
                    isError = showError && meaningText.isBlank()
                )

                OutlinedTextField(
                    value = exampleText,
                    onValueChange = { exampleText = it },
                    label = { Text("Ejemplo de uso práctico") },
                    placeholder = { Text("Situación real en una empresa o mercado...") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = formulaText,
                    onValueChange = { formulaText = it },
                    label = { Text("Fórmula o expresión (opcional)") },
                    placeholder = { Text("Ej. CMg = ΔCT / ΔQ") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = synonymsText,
                    onValueChange = { synonymsText = it },
                    label = { Text("Sinónimos (opcional, separados por coma)") },
                    placeholder = { Text("Ej. Costo alternativo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = antonymsText,
                    onValueChange = { antonymsText = it },
                    label = { Text("Antónimos (opcional, separados por coma)") },
                    placeholder = { Text("Ej. Beneficio absoluto") },
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
                        val newTerm = EcoTerm(
                            term = termText.trim(),
                            partOfSpeech = if (partOfSpeechText.isBlank()) "Sustantivo" else partOfSpeechText.trim(),
                            summary = if (meaningText.length > 90) meaningText.take(87) + "..." else meaningText.trim(),
                            meaning = meaningText.trim(),
                            example = if (exampleText.isBlank()) "Aplicación práctica según la teoría microeconómica." else exampleText.trim(),
                            formula = if (formulaText.isBlank()) null else formulaText.trim(),
                            module = selectedModule,
                            week = weekNumber.toIntOrNull() ?: 1,
                            category = if (categoryText.isBlank()) "General" else categoryText.trim(),
                            bibliography = "Aporte de usuario UMG",
                            synonyms = if (synonymsText.isBlank()) null else synonymsText.trim(),
                            antonyms = if (antonymsText.isBlank()) null else antonymsText.trim()
                        )
                        onSaveTerm(newTerm)
                    }
                },
                modifier = Modifier.testTag("save_new_term_button")
            ) {
                Text("Guardar Palabra")
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
