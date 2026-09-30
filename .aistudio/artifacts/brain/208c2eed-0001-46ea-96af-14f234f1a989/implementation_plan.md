# Diccionario de Microeconomía (UMG Ingeniería en Sistemas)

Aplicación Android nativa desarrollada en Jetpack Compose para estudiantes de Microeconomía (Facultad de Ingeniería en Sistemas, Universidad Mariano Gálvez de Guatemala), orientada a consultar, aprender y repasar terminología económica con búsqueda por voz, palabra del día, ejemplos prácticos y favoritos persistentes.

---

## Decisiones Críticas y Resumen de Especificación

> [!IMPORTANT]
> El contenido y la arquitectura del diccionario están directamente fundamentados en la **Guía Didáctica de Microeconomía (Código 016)** y la bibliografía oficial del curso (*Piloña Ortiz, Samuelson, Hernández Andrade*).

- **Estructura Temática**: Clasificación por los 3 módulos oficiales del curso:
  1. *Módulo I: Introducción a la Economía y Teoría del Consumidor* (Semanas 1 a 5: conceptos fundamentales, curvas de utilidad total y marginal, curvas de indiferencia).
  2. *Módulo II: Tasas Marginales, Oferta y Demanda* (Semanas 7 a 11: Tasa Marginal de Sustitución TMS, línea presupuestaria, curva de Engel, curvas precio-consumo, equilibrio de mercado).
  3. *Módulo III: Elasticidad, Teoría de la Producción y Mercado* (Semanas 13 a 17: elasticidades, producto medio/marginal, rendimientos decrecientes, estructuras de mercado y competencia).
- **Entrada por Voz**: Integración nativa de `RecognizerIntent.ACTION_RECOGNIZE_SPEECH` con fallback manual si el servicio no está disponible en el dispositivo.
- **Pronunciación y Audio**: Botón de Text-to-Speech (TTS) nativo en español para escuchar la definición de cada término.
- **Persistencia**: Base de datos local Room para almacenar el catálogo completo de términos y el estado de favoritos guardados por el estudiante.

---

## 1. Visión General y Concepto

### Propósito del Producto
Dotar a los estudiantes de Ingeniería en Sistemas de una herramienta de consulta ágil y precisa para comprender conceptos microeconómicos abstractos (curvas, tasas marginales, elasticidades y estructuras de mercado), facilitando la preparación para los exámenes parciales (10 y 20 pts) y el examen final (35 pts).

### Casos de Uso Principales
1. **Búsqueda Instantánea**: Encontrar cualquier término escribiendo o hablando por el micrófono.
2. **Palabra del Día**: Descubrir un término económico nuevo cada día con base en un algoritmo determinista por fecha calendario.
3. **Explorador por Módulos Temáticos**: Filtrar y estudiar términos organizados según las semanas lectivas del programa.
4. **Ficha de Estudio Detallada**: Visualizar definición rigurosa, fórmula o gráfica asociada, ejemplo aplicado en el mundo real y guardar en Favoritos con un toque.

---

## 2. Experiencia de Usuario y Diseño Visual

### Identidad Visual y Estilo
- **Paleta de Colores**: 
  - *Color Primario*: Azul Académico Profundo (`#0D2F4F` / `#1E3A8A`) evocando rigor universitario e identidad institucional UMG.
  - *Color Secundario / Acento*: Verde Esmeralda Económico (`#059669` / `#10B981`) representando finanzas, valor y crecimiento.
  - *Fondo y Superficies*: Tonos marfil y pizarra suave (`#F8FAFC` en modo claro, `#0F172A` en modo oscuro) con tarjetas elevadas M3.
- **Tipografía**: Jerarquía clara con títulos en sans-serif de peso bold y cuerpo de texto optimizado para lectura cómoda de definiciones y fórmulas matemáticas.

### Flujo de Navegación
```
┌────────────────────────────────────────────────────────┐
│                   PANTALLA DE INICIO                   │
│ • Barra de búsqueda con icono de Micrófono             │
│ • Tarjeta destacada: "Palabra del Día"                 │
│ • Acceso rápido a Favoritos y Estadísticas de repaso   │
│ • Filtros rápidos por Módulos I, II, III               │
│ • Botón principal: "Explorar Todas las Palabras"       │
└───────────────────────────┬────────────────────────────┘
                            │
              ┌─────────────┴─────────────┐
              ▼                           ▼
┌───────────────────────────┐ ┌───────────────────────────┐
│     LISTA / EXPLORADOR    │ │    DETALLE DEL TÉRMINO    │
│ • Índice A-Z              │ │ • Nombre y Módulo/Semana  │
│ • Filtros por Módulo      │ │ • Botón de Favorito (Top) │
│ • Buscador en tiempo real │ │ • Botón de Audio (TTS)    │
│ • Tarjetas con resumen    │ │ • Definición académica    │
└─────────────┬─────────────┘ │ • Ejemplo del mundo real  │
              │               │ • Fórmula / Gráfico clave │
              └──────────────►│ • Términos relacionados   │
                              └───────────────────────────┘
```

---

## 3. Decisiones Técnicas Clave y Compensaciones

1. **Reconocimiento de Voz Nativo (`SpeechRecognizer` / `RecognizerIntent`)**:
   - *Decisión*: Usar el lanzador de actividad estándar de Android para reconocimiento de voz, procesando el texto transcrito directamente en la barra de búsqueda.
   - *Ventaja*: Cero latencia externa, no requiere API keys ni servicios de pago, funciona con el motor de voz del sistema del usuario.

2. **Base de Datos Local con Room**:
   - *Decisión*: Room Database precargada con términos económicos extraídos del programa de estudios de UMG y bibliografía de Piloña Ortiz y Samuelson.
   - *Ventaja*: Disponibilidad 100% offline (vital para el aula o laboratorios sin internet), velocidad instantánea de búsqueda e indexación para favoritos.

3. **Arquitectura y Estado (MVVM + StateFlow)**:
   - *Decisión*: `DictionaryViewModel` administrando `uiState: StateFlow<DictionaryUiState>` con filtrado reactivo (`combine` de query, módulo seleccionado y lista de favoritos).

---

## 4. Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────────────┐
│                     Jetpack Compose UI                          │
│  HomeScreen ─── ExplorerScreen ─── TermDetailScreen ── Theme.kt │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│              ViewModel (DictionaryViewModel)                    │
│   • searchQuery & isListening (Speech)                          │
│   • wordOfTheDay (Calendar date seed)                           │
│   • selectedModuleFilter                                        │
│   • favoritesList & toggleFavorite()                            │
│   • textToSpeech speakDefinition()                              │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Room Database Layer                           │
│   • EconomicTerm (Entity: id, term, definition, example,        │
│                   formula, module, week, isFavorite)            │
│   • TermDao (Query, searchTerms, getFavorites, updateFavorite)  │
│   • AppDatabase & TermRepository                                │
└─────────────────────────────────────────────────────────────────┘
```

---

## 5. Plan de Ejecución por Fases (Una vez aprobado)

1. **Configuración y Base de Datos**:
   - Definir la entidad `EconomicTerm`, el `TermDao` y la base de datos `AppDatabase` con Room.
   - Precargar el catálogo inicial con más de 30 términos esenciales de Microeconomía distribuidos en los 3 módulos del programa.
2. **Capa de Lógica (Repository, TTS & ViewModel)**:
   - Implementar `TermRepository` y `DictionaryViewModel`.
   - Lógica de la *Palabra del Día* basada en la fecha del dispositivo.
   - Integración del reconocedor de voz por micrófono y síntesis de voz (TextToSpeech).
3. **Interfaz de Usuario (Jetpack Compose M3)**:
   - `HomeScreen`: Barra de búsqueda con micrófono, tarjeta animada de Palabra del Día, chips de módulos y botón de exploración.
   - `ExplorerScreen`: Lista filtrable por A-Z, búsqueda en vivo y pestañas de módulos.
   - `TermDetailScreen`: Vista rica del concepto con botón de favorito en la esquina superior derecha, reproductor de audio, definición, ejemplos prácticos y fórmulas.
4. **Verificación y Compilación**:
   - Ejecutar `compile_applet` para garantizar cero advertencias y funcionamiento fluido en el emulador.
