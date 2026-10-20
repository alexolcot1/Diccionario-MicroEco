package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch

data class TutorialHighlight(
    val icon: ImageVector,
    val iconTint: Color,
    val label: String,
    val detail: String
)

data class TutorialStep(
    val stepIndex: Int,
    val badge: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val iconTint: Color,
    val highlights: List<TutorialHighlight>
)

@Composable
fun AppTutorialDialog(
    onDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Step definitions carefully sequenced for maximum pedagogical clarity
    val steps = listOf(
        // 1. Bienvenida e Identidad UMG
        TutorialStep(
            stepIndex = 1,
            badge = "Guía Oficial UMG",
            title = "¡Bienvenido a MicroDiccionario!",
            subtitle = "Tu guía de Microeconomía UMG",
            description = "Aplicación oficial diseñada para estudiantes y docentes de la Universidad Mariano Gálvez de Guatemala. Accede a más de 250 conceptos económicos esenciales con pronunciación en voz alta, ejemplos reales y herramientas interactivas.",
            icon = Icons.Default.MenuBook,
            iconBgColor = Color(0xFF002B49),
            iconTint = Color(0xFFFFD54F),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.MenuBook,
                    iconTint = Color(0xFF002B49),
                    label = "250+ Términos Esenciales",
                    detail = "Definiciones científicas adaptadas a la guía didáctica oficial."
                ),
                TutorialHighlight(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = Color(0xFFB38E32),
                    label = "100% Sin Conexión",
                    detail = "Toda la base de datos se guarda en tu teléfono; no consume tus datos móviles."
                )
            )
        ),

        // 2. Palabra del Día y el Botón del Dado
        TutorialStep(
            stepIndex = 2,
            badge = "Pantalla de Inicio",
            title = "Palabra del Día y Dado Aleatorio",
            subtitle = "Descubre nuevos conceptos cada día",
            description = "En la pantalla de Inicio encontrarás la tarjeta de la 'Palabra del Día' con su concepto clave y síntesis explicativa.",
            icon = Icons.Default.Casino,
            iconBgColor = Color(0xFF002B49),
            iconTint = Color(0xFFFFD54F),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.Casino,
                    iconTint = Color(0xFFE65100),
                    label = "Botón del Dado 🎲",
                    detail = "Toca el dado en la esquina superior de la tarjeta para cambiarla inmediatamente por otra palabra aleatoria al azar."
                ),
                TutorialHighlight(
                    icon = Icons.Default.Bookmark,
                    iconTint = Color(0xFFB38E32),
                    label = "Guardar en Favoritos ⭐",
                    detail = "Presiona la estrella para añadir la palabra a tu lista de estudio."
                ),
                TutorialHighlight(
                    icon = Icons.Default.VolumeUp,
                    iconTint = Color(0xFF002B49),
                    label = "Pronunciación en Voz Alta",
                    detail = "Escucha la pronunciación clara y natural con el motor de voz en español."
                )
            )
        ),

        // 3. Búsqueda Rápida y Reconocimiento de Voz
        TutorialStep(
            stepIndex = 3,
            badge = "Buscador Inteligente",
            title = "Búsqueda Instantánea y por Voz",
            subtitle = "Encuentra cualquier término en segundos",
            description = "El buscador en tiempo real te permite localizar conceptos alfabéticamente o por palabras clave dentro de su significado.",
            icon = Icons.Default.Search,
            iconBgColor = Color(0xFF0A436D),
            iconTint = Color(0xFFFFFFFF),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.Search,
                    iconTint = Color(0xFF0A436D),
                    label = "Escritura Continua Fluida",
                    detail = "El teclado virtual se mantiene siempre abierto mientras escribes; los resultados se filtran al instante."
                ),
                TutorialHighlight(
                    icon = Icons.Default.Mic,
                    iconTint = Color(0xFFE65100),
                    label = "Búsqueda por Voz 🎙️",
                    detail = "Toca el icono del micrófono en la barra y dicta el término (ej. 'Elasticidad') para buscar sin escribir."
                ),
                TutorialHighlight(
                    icon = Icons.Default.Close,
                    iconTint = Color(0xFF757575),
                    label = "Botón Limpiar (X)",
                    detail = "Borra el texto rápidamente para regresar al inicio."
                )
            )
        ),

        // 4. Dónde están las palabras: Explorador A-Z
        TutorialStep(
            stepIndex = 4,
            badge = "¿Dónde están las palabras?",
            title = "Catálogo Alfabético Completo",
            subtitle = "Explora los 250 términos en la pestaña 'Explorar'",
            description = "¿Deseas ver todo el glosario de términos? En la barra inferior, ingresa a la pestaña 'Explorar' para navegar por el diccionario ordenado de la A a la Z.",
            icon = Icons.Default.MenuBook,
            iconBgColor = Color(0xFFB38E32),
            iconTint = Color(0xFFFFFFFF),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.MenuBook,
                    iconTint = Color(0xFF002B49),
                    label = "Chips Alfabéticos (A - Z)",
                    detail = "Desplázate horizontalmente por las letras (A, B, C...) y pulsa cualquier inicial para filtrar al instante."
                ),
                TutorialHighlight(
                    icon = Icons.Default.Bookmark,
                    iconTint = Color(0xFFB38E32),
                    label = "Ficha Detallada",
                    detail = "Toca cualquier tarjeta para abrir su vista completa con definición rigurosa y ejemplos de aplicación."
                )
            )
        ),

        // 5. Gestión de Palabras: Agregar Nueva Palabra
        TutorialStep(
            stepIndex = 5,
            badge = "Personalización",
            title = "Cómo Agregar Nuevas Palabras",
            subtitle = "Amplía tu diccionario con apuntes propios",
            description = "Puedes incorporar términos adicionales, conceptos explicados en clase o notas dictadas por tu catedrático.",
            icon = Icons.Default.Add,
            iconBgColor = Color(0xFF1B5E20),
            iconTint = Color(0xFFFFFFFF),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.Add,
                    iconTint = Color(0xFF1B5E20),
                    label = "Botón Flotante en Inicio ➕",
                    detail = "En la pantalla de Inicio, toca el botón de tres puntos (kebab) y selecciona 'Agregar término'."
                ),
                TutorialHighlight(
                    icon = Icons.Default.Edit,
                    iconTint = Color(0xFF002B49),
                    label = "Formulario Completo",
                    detail = "Ingresa el nombre del término, categoría, resumen, significado y ejemplos prácticos."
                )
            )
        ),

        // 6. Gestión de Palabras: Editar y Borrar
        TutorialStep(
            stepIndex = 6,
            badge = "Edición y Control",
            title = "Cómo Editar y Borrar Palabras",
            subtitle = "Administra términos en la vista de detalle",
            description = "Cada ficha de término cuenta con herramientas para actualizar su contenido o eliminarlo de tu base de datos.",
            icon = Icons.Default.Edit,
            iconBgColor = Color(0xFF4A148C),
            iconTint = Color(0xFFFFFFFF),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.Edit,
                    iconTint = Color(0xFF0D47A1),
                    label = "Editar Término ✏️",
                    detail = "Abre cualquier palabra, toca el menú de opciones (kebab) y elige 'Editar' para corregir o agregar notas."
                ),
                TutorialHighlight(
                    icon = Icons.Default.DeleteOutline,
                    iconTint = Color(0xFFB71C1C),
                    label = "Borrar Término 🗑️",
                    detail = "Si deseas descartar una palabra agregada, selecciona 'Eliminar' con confirmación de seguridad."
                )
            )
        ),

        // 7. Flashcards de Repaso y Finalización
        TutorialStep(
            stepIndex = 7,
            badge = "¡Todo Listo para Aprender!",
            title = "Modo de Estudio con Flashcards",
            subtitle = "Tarjetas interactivas para tus exámenes",
            description = "En la pestaña 'Repaso' podrás ejercitar tu memoria con tarjetas volteables. Autoevalúate pulsando 'Lo sé' si dominas el concepto o 'Repasar' si necesitas reforzarlo.",
            icon = Icons.Default.AutoAwesome,
            iconBgColor = Color(0xFFE65100),
            iconTint = Color(0xFFFFFFFF),
            highlights = listOf(
                TutorialHighlight(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = Color(0xFFE65100),
                    label = "Giro de Tarjeta (Flip)",
                    detail = "Toca la tarjeta para dar la vuelta y descubrir la definición oculta."
                ),
                TutorialHighlight(
                    icon = Icons.Default.MenuBook,
                    iconTint = Color(0xFF002B49),
                    label = "Revisa el Tutorial cuando quieras",
                    detail = "En la pestaña 'Acerca', al final de la pantalla, pulsa el botón naranja 'Ver tutorial' para volver a consultarlo."
                )
            )
        )
    )

    val pagerState = rememberPagerState(pageCount = { steps.size })
    val isLastPage = pagerState.currentPage == steps.size - 1

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.72f))
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .testTag("app_tutorial_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 500.dp)
                    .heightIn(max = 680.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    // Top Bar: Step indicator + Skip Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Paso ${pagerState.currentPage + 1} de ${steps.size}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        TextButton(
                            onClick = onComplete,
                            modifier = Modifier.testTag("tutorial_skip_button")
                        ) {
                            Text(
                                text = "Omitir",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pager Content
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) { pageIndex ->
                        val step = steps[pageIndex]
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(6.dp))

                            // Step Icon Container with Badge
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(step.iconBgColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = step.icon,
                                    contentDescription = null,
                                    tint = step.iconTint,
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f)
                            ) {
                                Text(
                                    text = step.badge,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = step.subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = step.description,
                                style = MaterialTheme.typography.bodySmall,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Highlighted Features for this step
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                step.highlights.forEach { item ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(CircleShape)
                                                    .background(item.iconTint.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = item.icon,
                                                    contentDescription = null,
                                                    tint = item.iconTint,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(10.dp))

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = item.label,
                                                    style = MaterialTheme.typography.labelLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = item.detail,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    lineHeight = 16.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom Navigation Row: Indicator Dots + Back/Next buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button (if not on page 0)
                        if (pagerState.currentPage > 0) {
                            OutlinedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                modifier = Modifier.testTag("tutorial_prev_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Anterior",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Atrás")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(60.dp))
                        }

                        // Dot Indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(steps.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                val dotWidth by animateDpAsState(
                                    targetValue = if (isSelected) 22.dp else 7.dp,
                                    label = "dotWidth"
                                )
                                val dotColor by animateColorAsState(
                                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                    label = "dotColor"
                                )

                                Box(
                                    modifier = Modifier
                                        .height(7.dp)
                                        .width(dotWidth)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                        .clickable {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                )
                            }
                        }

                        // Next / Start Button
                        Button(
                            onClick = {
                                if (isLastPage) {
                                    onComplete()
                                } else {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLastPage) Color(0xFFE65100) else MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("tutorial_next_button")
                        ) {
                            Text(
                                text = if (isLastPage) "¡Comenzar!" else "Siguiente",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isLastPage) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
