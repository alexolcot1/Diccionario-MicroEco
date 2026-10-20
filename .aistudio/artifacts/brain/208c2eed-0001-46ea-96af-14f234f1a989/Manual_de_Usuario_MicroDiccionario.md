# MANUAL DE USUARIO OFICIAL
## MicroDiccionario de Economía • Guía Didáctica y Operativa
**Universidad Mariano Gálvez de Guatemala**  
*Facultad de Ciencias Económicas • Cátedra de Microeconomía*  
*Versión de Software: 1.0.0 Oficial (2026)*

---

### Archivos Oficiales Disponibles para Descargar y Subir a Google Drive:
- 📄 **`MANUAL_DE_USUARIO_MICRODICCIONARIO.docx`** (Documento Word con formato nativo para abrir y editar en **Google Docs**)
- 📕 **`MANUAL_DE_USUARIO_MICRODICCIONARIO.pdf`** (Documento PDF oficial de 5 páginas con logotipo UMG y paginado)
- 🌐 **`MANUAL_DE_USUARIO_MICRODICCIONARIO.html`** (Visor web interactivo para imprimir o leer en navegador)

---

## ☁️ CÓMO GUARDAR Y ABRIR ESTE MANUAL EN GOOGLE DRIVE

Para tener este manual accesible y editable en tu cuenta de **Google Drive / Google Docs**:

1. **Localiza el archivo en el explorador de archivos:**  
   En la lista de archivos del proyecto encontrarás `MANUAL_DE_USUARIO_MICRODICCIONARIO.docx` y `MANUAL_DE_USUARIO_MICRODICCIONARIO.pdf`.
2. **Descárgalo a tu computadora o teléfono:**  
   Haz clic derecho sobre el archivo y selecciona **Download / Descargar** (o expórtalo vía ZIP).
3. **Súbelo a tu Google Drive:**  
   - Abre [drive.google.com](https://drive.google.com) con tu cuenta de Google.
   - Haz clic en el botón **"+ Nuevo" > "Subir archivo"** (o arrastra directamente el archivo `.docx` a tu pantalla de Google Drive).
4. **Ábrelo con Google Docs:**  
   - Haz doble clic sobre el archivo `.docx` en tu Drive.
   - Google Drive te permitirá abrirlo directamente con **Documentos de Google (Google Docs)** con todo el formato, tipografías, tablas, llamadas y logotipo UMG intactos, listo para editar, añadir comentarios o compartir mediante enlace.

---

## 1. INTRODUCCIÓN Y PROPÓSITO DEL SISTEMA

**MicroDiccionario** es una aplicación móvil nativa para Android concebida como herramienta de apoyo didáctico para los cursos de Microeconomía y Teoría Económica de la Universidad Mariano Gálvez de Guatemala.

Su objetivo es proporcionar a la comunidad académica una referencia terminológica inmediata, precisa y científicamente rigurosa, acompañada de herramientas pedagógicas como síntesis de voz, ejemplos de aplicación real y tarjetas interactivas de memorización activa (Flashcards).

### 1.1 Características Clave
- **250 Términos Económicos:** Glosario completo clasificado alfabéticamente (de la A a la Z) con definiciones científicas y ejemplos cotidianos.
- **Búsqueda Instantánea con Teclado Persistente:** Buscador en vivo optimizado que no pierde foco ni cierra el teclado virtual al escribir.
- **Búsqueda por Reconocimiento de Voz:** Permite dictar términos mediante el motor de voz de Google.
- **Pronunciación y Lectura en Voz Alta (Text-to-Speech):** Reproducción auditiva para mejorar la comprensión y accesibilidad.
- **Módulo de Flashcards de Estudio:** Tarjetas dinámicas con autoevaluación (*"Lo sé"* vs *"Repasar"*) y barra de progreso.
- **Operación 100% Offline:** Toda la base de datos se almacena localmente en SQLite/Room; no consume datos móviles.

### 1.2 Requisitos del Sistema
- **Dispositivo:** Teléfono o tablet Android.
- **Versión de Android:** Android 8.0 (Oreo / API 26) o superior.
- **Almacenamiento:** Aproximadamente 45 MB de espacio disponible.

---

## 2. ARQUITECTURA DE NAVEGACIÓN

La aplicación cuenta con una barra de navegación inferior (*Bottom Navigation*) con 5 secciones principales:

| Icono | Sección | Descripción |
|---|---|---|
| 🏠 | **Inicio (Home)** | Buscador en vivo, Palabra del Día, identidad institucional UMG y catálogo filtrado. |
| 📖 | **Explorar (Explore)** | Catálogo alfabético completo con botones de letras (A-Z). |
| 📇 | **Flashcards (Estudio)** | Modo interactivo de tarjetas volteables con contador de aciertos. |
| ⭐ | **Favoritos (Favorites)** | Biblioteca personalizada de conceptos guardados para repaso. |
| ℹ️ | **Acerca de (About)** | Información académica de la UMG, créditos y cátedra de Microeconomía. |

---

## 3. GUÍA PASO A PASO POR MÓDULOS

### 3.1 Módulo de Inicio (Home) y Búsqueda
1. **Búsqueda Rápida por Texto:**
   - Toca la barra superior con el texto *"Buscar término o concepto..."*.
   - Comienza a teclear cualquier concepto (por ejemplo, *Costo*, *Demanda*, *Monopolio*).
   - Los resultados se filtran instantáneamente debajo mientras el teclado permanece abierto para una escritura cómoda y continua.
2. **Búsqueda por Reconocimiento de Voz:**
   - Toca el icono de **micrófono** ubicado en el extremo derecho de la barra de búsqueda.
   - Pronuncia con claridad el concepto deseado.
   - El sistema transcribirá tu voz y filtrará los resultados correspondientes.
3. **Limpiar Búsqueda:**
   - Al pulsar el icono **(X)** que aparece cuando hay texto escrito, la barra se vacía y se restaura la vista inicial.
4. **Palabra del Día:**
   - En la parte central de Inicio se muestra diariamente un concepto destacado con su definición y un botón de audio directo para escuchar la pronunciación.

---

### 3.2 Módulo de Exploración y Glosario Alfabético
1. Accede a la pestaña **Explorar** desde la barra inferior.
2. En la parte superior encontrarás una barra desplazable con letras: `Todos`, `A`, `B`, `C`, `D`, `E`, `F`, `G`...
3. Al pulsar una letra específica, la lista filtrará automáticamente mostrando únicamente los términos cuya inicial coincida.
4. Cada tarjeta muestra el nombre del término, su categoría y un resumen explicativo.
5. Toca cualquier tarjeta para abrir la ficha detallada.

---

### 3.3 Ficha Detallada de Concepto (Word Detail)
Al seleccionar un término, se despliega su pantalla completa con las siguientes secciones:
- **Título y Categoría:** Nombre formal del concepto y su clasificación alfabética.
- **Definición Rigurosa:** Marco teórico y conceptual según la ciencia económica.
- **Ejemplo Práctico:** Aplicación tangible en los mercados, empresas o decisiones del consumidor.
- **Botón de Audio (TTS):** Al pulsar el icono flotante de altavoz, la app lee la definición en voz alta en español.
- **Botón de Favoritos (Estrella):** Toca la estrella en la esquina superior para guardar o retirar el término de tu lista personal.
- **Copiar y Compartir:** Permite copiar la definición al portapapeles para pegarla en tus resúmenes o chats de estudio.

---

### 3.4 Módulo de Estudio Interactivo (Flashcards)
Diseñado para la preparación de exámenes parciales y finales mediante el principio de repetición activa:
1. Dirígete a la pestaña **Flashcards**.
2. **Frente de la Tarjeta:** Lee el término económico propuesto e intenta recordar su significado.
3. **Girar Tarjeta (Flip):** Toca cualquier parte de la tarjeta para girarla y revelar la definición en el reverso.
4. **Autoevaluación:**
   - **Botón Verde ("Lo sé"):** Registra el concepto como aprendido e incrementa el contador de dominio.
   - **Botón Ámbar ("Repasar"):** Mantiene el término en la cola de repaso para reforzarlo más tarde.
5. **Barra de Progreso:** Indica en tiempo real cuántas tarjetas has dominado sobre el total.

---

### 3.5 Biblioteca de Favoritos
- Permite concentrar únicamente los conceptos que te resulten más complejos o prioritarios.
- Al pulsar sobre cualquier término guardado se abre su ficha completa.
- Para retirar un término de Favoritos, simplemente vuelve a presionar el icono de la estrella rellena.

---

### 3.6 Sección Institucional (Acerca de)
Contiene la identificación oficial de la **Universidad Mariano Gálvez de Guatemala**, los propósitos didácticos del curso de Microeconomía y la información de versión del software.

---

## 4. PREGUNTAS FRECUENTES (FAQ) Y SOPORTE TÉCNICO

**P: ¿Por qué el teclado no se cierra cuando escribo en el buscador?**  
*R:* Se rediseñó la estructura de la pantalla para garantizar que la barra de búsqueda sea persistente. El campo de texto ya no se destruye ni pierde el foco al escribir letras consecutivas, ofreciendo una experiencia fluida.

**P: ¿Qué hacer si no se escucha el audio al pulsar el botón de sonido?**  
*R:* 
1. Verifica que el volumen de **Multimedia** de tu teléfono no esté en silencio o en vibración.
2. Ve a los *Ajustes de Android > Sistema / Accesibilidad > Síntesis de voz (Text-to-Speech)* y comprueba que el motor de voz de Google esté activo con el idioma Español seleccionado.

**P: ¿La aplicación funciona sin conexión a internet?**  
*R:* Sí. La base de datos de los 250 términos económicos y el sistema de flashcards están integrados de forma local en la aplicación, por lo que puedes estudiar en cualquier momento sin consumir datos.
