const fs = require('fs');
const path = require('path');
const docx = require('/tmp/node_modules/docx');
const PDFDocument = require('/tmp/node_modules/pdfkit/js/pdfkit.js');

const {
  Document,
  Packer,
  Paragraph,
  TextRun,
  HeadingLevel,
  Table,
  TableRow,
  TableCell,
  WidthType,
  AlignmentType,
  BorderStyle,
  ShadingType,
  Header,
  Footer,
  PageNumber,
  ImageRun
} = docx;

console.log('Starting Manual generation script...');

// Colors
const COLOR_PRIMARY = '002B49'; // Navy UMG
const COLOR_SECONDARY = 'B38E32'; // Gold UMG
const COLOR_TEXT = '1F2937'; // Slate 800
const COLOR_BG_CALLOUT = 'F0F4F8'; // Light Blue-Gray
const COLOR_BORDER = 'CBD5E1';

// Read UMG logo
let logoBuffer = null;
const logoPath = path.resolve('app/src/main/res/drawable/logo_umg.png');
if (fs.existsSync(logoPath)) {
  logoBuffer = fs.readFileSync(logoPath);
}

// -------------------------------------------------------------
// 1. GENERATE DOCX FILE (FOR GOOGLE DOCS / GOOGLE DRIVE)
// -------------------------------------------------------------
async function generateDocx() {
  const createTitle = (text) => new Paragraph({
    text: text,
    heading: HeadingLevel.TITLE,
    alignment: AlignmentType.CENTER,
    spacing: { before: 240, after: 120 }
  });

  const createHeading1 = (text) => new Paragraph({
    text: text,
    heading: HeadingLevel.HEADING_1,
    spacing: { before: 360, after: 140 },
    border: {
      bottom: { color: COLOR_PRIMARY, space: 4, style: BorderStyle.SINGLE, size: 12 }
    }
  });

  const createHeading2 = (text) => new Paragraph({
    text: text,
    heading: HeadingLevel.HEADING_2,
    spacing: { before: 240, after: 100 }
  });

  const createHeading3 = (text) => new Paragraph({
    text: text,
    heading: HeadingLevel.HEADING_3,
    spacing: { before: 180, after: 80 }
  });

  const createP = (text, options = {}) => new Paragraph({
    children: [new TextRun({ text: text, color: COLOR_TEXT, size: 22, ...options })],
    spacing: { after: 120, line: 276 }
  });

  const createBullet = (label, desc) => new Paragraph({
    children: [
      new TextRun({ text: `• ${label}: `, bold: true, color: COLOR_PRIMARY, size: 22 }),
      new TextRun({ text: desc, color: COLOR_TEXT, size: 22 })
    ],
    spacing: { after: 80, line: 276 },
    indent: { left: 360 }
  });

  const createCallout = (title, body) => new Table({
    width: { size: 100, type: WidthType.PERCENTAGE },
    borders: {
      top: { style: BorderStyle.NONE },
      bottom: { style: BorderStyle.NONE },
      right: { style: BorderStyle.NONE },
      left: { style: BorderStyle.SINGLE, size: 24, color: COLOR_SECONDARY }
    },
    rows: [
      new TableRow({
        children: [
          new TableCell({
            shading: { fill: COLOR_BG_CALLOUT, type: ShadingType.CLEAR },
            margins: { top: 140, bottom: 140, left: 180, right: 180 },
            children: [
              new Paragraph({
                children: [new TextRun({ text: title, bold: true, color: COLOR_PRIMARY, size: 22 })],
                spacing: { after: 60 }
              }),
              new Paragraph({
                children: [new TextRun({ text: body, color: COLOR_TEXT, size: 20 })]
              })
            ]
          })
        ]
      })
    ]
  });

  const docChildren = [];

  // Cover Page
  if (logoBuffer) {
    docChildren.push(new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [
        new ImageRun({
          data: logoBuffer,
          transformation: { width: 140, height: 140 }
        })
      ],
      spacing: { before: 360, after: 240 }
    }));
  }

  docChildren.push(
    new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [
        new TextRun({
          text: "UNIVERSIDAD MARIANO GÁLVEZ DE GUATEMALA",
          bold: true,
          size: 28,
          color: COLOR_PRIMARY
        })
      ],
      spacing: { after: 80 }
    }),
    new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [
        new TextRun({
          text: "Facultad de Ciencias Económicas • Cátedra de Microeconomía",
          italics: true,
          size: 22,
          color: COLOR_SECONDARY
        })
      ],
      spacing: { after: 360 }
    }),
    new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [
        new TextRun({
          text: "MANUAL DE USUARIO OFICIAL",
          bold: true,
          size: 40,
          color: COLOR_PRIMARY
        })
      ],
      spacing: { before: 240, after: 120 }
    }),
    new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [
        new TextRun({
          text: "Aplicación Móvil: MicroDiccionario de Economía",
          bold: true,
          size: 26,
          color: COLOR_SECONDARY
        })
      ],
      spacing: { after: 100 }
    }),
    new Paragraph({
      alignment: AlignmentType.CENTER,
      children: [
        new TextRun({
          text: "Guía Didáctica y Operativa Integral para Estudiantes, Catedráticos y Usuarios Finales",
          size: 20,
          color: "64748B"
        })
      ],
      spacing: { after: 480 }
    }),
    createCallout(
      "INFORMACIÓN DEL DOCUMENTO",
      "Versión: 1.0 Oficial | Edición: 2026 | Compatibilidad: Android 8.0+ | Plataforma: Material Design 3\nEste manual está optimizado para importarse y abrirse directamente en Google Docs desde Google Drive."
    ),
    new Paragraph({ text: "", pageBreakBefore: true })
  );

  // Table of Contents
  docChildren.push(
    createHeading1("ÍNDICE GENERAL"),
    createBullet("1. Introducción y Propósito", "Objetivos pedagógicos, características principales y requisitos técnicos."),
    createBullet("2. Arquitectura de Navegación", "Estructura de menús y acceso a las 5 secciones principales."),
    createBullet("3. Módulo de Inicio (Home)", "Búsqueda rápida en tiempo real, búsqueda por voz, Palabra del Día y menú rápido."),
    createBullet("4. Módulo Explorar (Glosario A-Z)", "Directorio completo de 250 términos económicos con filtro alfabético dinámico."),
    createBullet("5. Ficha Detallada de Concepto", "Definición rigurosa, ejemplos prácticos, pronunciación TTS de voz y favoritos."),
    createBullet("6. Módulo de Flashcards (Estudio)", "Técnica de repetición espaciada, tarjetas volteables, autoevaluación y progreso."),
    createBullet("7. Módulo de Favoritos", "Gestión de términos guardados y preparación para evaluaciones académicas."),
    createBullet("8. Sección Institucional (Acerca de)", "Información académica de la UMG y créditos del proyecto."),
    createBullet("9. Preguntas Frecuentes y Soporte", "Resolución de incidencias, funcionamiento offline y configuración de audio."),
    createBullet("10. Cómo Guardar y Editar en Google Drive", "Pasos sencillos para tener este manual siempre disponible en Google Docs."),
    new Paragraph({ text: "", pageBreakBefore: true })
  );

  // Section 1: Introduction
  docChildren.push(
    createHeading1("1. INTRODUCCIÓN Y PROPÓSITO DEL SISTEMA"),
    createP("MicroDiccionario es una aplicación móvil nativa para el sistema operativo Android concebida como herramienta de apoyo didáctico para los cursos de Microeconomía y Teoría Económica de la Universidad Mariano Gálvez de Guatemala. Su objetivo primordial es brindar a la comunidad académica una referencia terminológica inmediata, precisa y científicamente rigurosa."),
    createP("La aplicación integra más de 250 conceptos fundamentales organizados sistemáticamente, abarcando desde nociones básicas como 'Abundancia' y 'Actividad económica' hasta conceptos avanzados de estructuras de mercado, costos marginales, teoría del consumidor y equilibrio general."),
    createHeading2("1.1 Características Clave"),
    createBullet("250 Términos Económicos", "Definiciones claras, completas y acompañadas de ejemplos prácticos aplicados a la realidad."),
    createBullet("Búsqueda Instantánea con Teclado Persistente", "Buscador optimizado que no pierde foco ni cierra el teclado durante la escritura."),
    createBullet("Búsqueda por Reconocimiento de Voz", "Permite dictar el término deseado utilizando el motor de voz de Android."),
    createBullet("Síntesis de Audio (Text-to-Speech)", "Pronunciación auditiva de definiciones para mejorar la retención y la accesibilidad."),
    createBullet("Módulo de Flashcards de Estudio", "Tarjetas interactivas con sistema de autoevaluación ('Lo sé' / 'Repasar')."),
    createBullet("Funcionamiento 100% Offline", "Todos los términos residen en la base de datos interna local (Room), sin requerir conexión a internet obligatoria."),
    createHeading2("1.2 Requisitos del Dispositivo"),
    createBullet("Sistema Operativo", "Android 8.0 (Oreo / API 26) o superior."),
    createBullet("Espacio en Almacenamiento", "Aproximadamente 45 MB."),
    createBullet("Permisos de Voz", "Permiso de micrófono (RECORD_AUDIO) opcional únicamente si se desea usar búsqueda por voz."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 2: Navigation
  docChildren.push(
    createHeading1("2. ARQUITECTURA Y NAVEGACIÓN"),
    createP("La aplicación cuenta con una barra de navegación inferior (Bottom Navigation Bar) fijada en la parte inferior de la pantalla, que permite alternar fluidamente entre las 5 vistas esenciales:"),
    createBullet("Inicio (Home)", "Acceso directo al buscador principal, a la Palabra del Día, al escudo UMG y al listado filtrado."),
    createBullet("Explorar (Explore)", "Catálogo alfabético completo con botones rápidos de letras de la A a la Z."),
    createBullet("Estudio (Flashcards)", "Entrenador interactivo con tarjetas de memoria para afianzar conocimientos."),
    createBullet("Favoritos (Favorites)", "Colección personalizada de términos seleccionados por el estudiante."),
    createBullet("Acerca de (About)", "Datos institucionales y académicos de la Universidad Mariano Gálvez de Guatemala."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 3: Home Module
  docChildren.push(
    createHeading1("3. MÓDULO DE INICIO (HOME) Y BÚSQUEDA"),
    createP("La pantalla principal sirve como centro de mando de la aplicación. Al abrir la app, el usuario es recibido por el encabezado institucional de la UMG y la barra de búsqueda rápida."),
    createHeading2("3.1 Barra de Búsqueda Rápida"),
    createP("El buscador de MicroDiccionario ha sido calibrado para una respuesta ultra-rápida en tiempo real:"),
    createBullet("Escritura Continua", "Al pulsar sobre la barra de búsqueda y comenzar a teclear, la lista de resultados filtra al instante mientras el teclado virtual permanece siempre activo y visible."),
    createBullet("Búsqueda por Voz", "Al tocar el icono de micrófono ubicado a la derecha de la barra, se abre el diálogo de reconocimiento de voz de Google. Simplemente pronuncie el término (por ejemplo, 'Elasticidad') y la búsqueda se ejecutará automáticamente."),
    createBullet("Botón Limpiar (X)", "Aparece automáticamente cuando hay texto escrito. Un toque borra todo el contenido y regresa a la vista general."),
    createHeading2("3.2 Tarjeta de la Palabra del Día"),
    createP("Cada día, el sistema selecciona automáticamente un término económico destacado con su resumen conceptual y un botón de reproducción auditiva directa. Esto fomenta el hábito de estudio diario sin abrumar al estudiante."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 4: Explore Module
  docChildren.push(
    createHeading1("4. MÓDULO DE EXPLORACIÓN Y GLOSARIO ALFABÉTICO"),
    createP("La sección Explorar permite consultar la totalidad de los 250 términos económicos clasificados rigurosamente de la letra A a la Z."),
    createHeading2("4.1 Filtro por Chips Alfabéticos"),
    createP("En la parte superior se visualiza un carrusel horizontal con chips seleccionables: 'Todos', 'A', 'B', 'C', 'D', 'E', 'F', 'G', etc. Al pulsar cualquier letra, la lista se desplaza y filtra automáticamente mostrando únicamente los conceptos correspondientes a esa inicial."),
    createHeading2("4.2 Tarjetas de Términos"),
    createP("Cada término se presenta en una tarjeta con su nombre en negrita, categoría ('Letra A', 'Letra B', etc.) y un resumen descriptivo breve. Al tocar la tarjeta, se abre la vista completa del término."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 5: Word Detail
  docChildren.push(
    createHeading1("5. FICHA DETALLADA DE CONCEPTO"),
    createP("La pantalla de detalle proporciona una inmersión completa en el concepto seleccionado:"),
    createBullet("Definición Científica", "Explicación teórica precisa bajo el marco analítico de la microeconomía."),
    createBullet("Ejemplo Práctico", "Caso de aplicación concreta en el mercado real para facilitar la comprensión práctica."),
    createBullet("Pronunciación y Lectura en Voz Alta (TTS)", "Pulsando el botón flotante de audio, la app lee en español la definición y el ejemplo."),
    createBullet("Botón de Favoritos (Estrella)", "Permite guardar el término en la biblioteca personal para estudiarlo posteriormente."),
    createBullet("Compartir y Copiar", "Facilita copiar la definición al portapapeles para citarla en ensayos o enviarla a compañeros."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 6: Flashcards Study
  docChildren.push(
    createHeading1("6. MÓDULO DE ESTUDIO INTERACTIVO (FLASHCARDS)"),
    createP("El módulo de Flashcards implementa la metodología de estudio activo recomendada por pedagogos para la asimilación de vocabulario técnico."),
    createHeading2("6.1 Mecánica de la Tarjeta"),
    createBullet("Frente de la Tarjeta", "Muestra el término económico a definir (ejemplo: 'Costo de oportunidad') y la categoría."),
    createBullet("Giro de la Tarjeta (Flip)", "Al dar un toque en cualquier parte de la tarjeta, ésta gira con una animación fluida revelando el reverso con la definición completa."),
    createHeading2("6.2 Sistema de Autoevaluación"),
    createBullet("Botón 'Lo sé' (Verde)", "Registra el concepto como dominado, incrementa el contador de aciertos y avanza al siguiente término."),
    createBullet("Botón 'Repasar' (Ámbar)", "Conserva el término para una posterior revisión sin penalizar al estudiante."),
    createBullet("Barra de Progreso", "Indica visualmente cuántas tarjetas han sido estudiadas respecto al total."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 7: Favorites
  docChildren.push(
    createHeading1("7. BIBLIOTECA DE FAVORITOS"),
    createP("La pestaña de Favoritos almacena los términos que el usuario ha marcado con una estrella. Es la herramienta ideal para preparar exámenes parciales o finales."),
    createBullet("Acceso Inmediato", "Permite repasar únicamente los conceptos difíciles o prioritarios sin buscar en el glosario completo."),
    createBullet("Desmarcar", "Al tocar la estrella rellena en cualquier tarjeta, el término se retira inmediatamente de la lista."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 8: Institutional Section
  docChildren.push(
    createHeading1("8. SECCIÓN INSTITUCIONAL (ACERCA DE)"),
    createP("Esta pantalla rinde tributo a la identidad académica de la Universidad Mariano Gálvez de Guatemala. Presenta la misión formativa de la institución, la acreditación de la Facultad de Ciencias Económicas y los créditos correspondientes al desarrollo del proyecto didáctico."),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 9: FAQ & Troubleshooting
  docChildren.push(
    createHeading1("9. PREGUNTAS FRECUENTES Y SOLUCIÓN DE PROBLEMAS"),
    createCallout(
      "P: ¿Por qué el teclado no se cierra cuando escribo en el buscador?",
      "R: Este comportamiento fue optimizado intencionalmente. Anteriormente, el teclado se cerraba tras escribir la primera letra porque la pantalla se redibujaba. Ahora la barra de búsqueda es permanente y mantiene el foco activo para que puedas escribir palabras completas con total comodidad."
    ),
    new Paragraph({ spacing: { after: 120 } }),
    createCallout(
      "P: ¿Qué hacer si no se escucha la voz al pulsar el botón de audio?",
      "R: 1. Comprueba que el volumen de 'Multimedia' en tu teléfono no esté en silencio.\n2. Verifica en Ajustes de Android > Accesibilidad / Sistema > 'Texto a voz (TTS)' que el motor de voz de Google en idioma Español esté instalado."
    ),
    new Paragraph({ spacing: { after: 120 } }),
    createCallout(
      "P: ¿La aplicación consume datos de internet?",
      "R: No. Toda la base de datos de 250 términos económicos está almacenada localmente en tu teléfono. Únicamente la búsqueda por voz puede requerir conexión si tu dispositivo no tiene descargado el paquete de reconocimiento de voz sin conexión de Google."
    ),
    new Paragraph({ spacing: { after: 180 } })
  );

  // Section 10: How to upload to Google Drive / Google Docs
  docChildren.push(
    createHeading1("10. CÓMO GUARDAR Y ABRIR ESTE MANUAL EN GOOGLE DRIVE"),
    createP("Para tener este manual accesible desde cualquier computadora, teléfono o tablet en tu cuenta de Google Drive:"),
    createBullet("Paso 1: Descargar el archivo", "Descarga el archivo 'MANUAL_DE_USUARIO_MICRODICCIONARIO.docx' (o el PDF) desde los archivos del proyecto."),
    createBullet("Paso 2: Entrar a Google Drive", "Ingresa en tu navegador web a https://drive.google.com con tu cuenta de correo."),
    createBullet("Paso 3: Subir el documento", "Haz clic en el botón '+ Nuevo' > 'Subir archivo' (o simplemente arrastra el archivo .docx dentro de la ventana de tu Drive)."),
    createBullet("Paso 4: Abrir con Google Docs", "Haz doble clic sobre el archivo subido y selecciona 'Abrir con Documentos de Google'. El archivo se abrirá con formato nativo de Google Docs, permitiéndote editarlo, añadir notas, imprimirlo o compartirlo con un enlace."),
    new Paragraph({ spacing: { after: 240 } })
  );

  const doc = new Document({
    title: "Manual de Usuario - MicroDiccionario UMG",
    description: "Manual de Usuario Oficial para la aplicación móvil MicroDiccionario de Economía UMG",
    creator: "Universidad Mariano Gálvez de Guatemala",
    sections: [
      {
        properties: {
          page: {
            margin: { top: 1200, bottom: 1200, left: 1400, right: 1400 }
          }
        },
        headers: {
          default: new Header({
            children: [
              new Paragraph({
                alignment: AlignmentType.RIGHT,
                children: [
                  new TextRun({
                    text: "MicroDiccionario UMG • Manual de Usuario Oficial",
                    color: "94A3B8",
                    size: 18
                  })
                ]
              })
            ]
          })
        },
        footers: {
          default: new Footer({
            children: [
              new Paragraph({
                alignment: AlignmentType.CENTER,
                children: [
                  new TextRun({ text: "Página ", color: "94A3B8", size: 18 }),
                  new TextRun({
                    children: [PageNumber.CURRENT],
                    color: "94A3B8",
                    size: 18
                  }),
                  new TextRun({ text: " | Universidad Mariano Gálvez de Guatemala", color: "94A3B8", size: 18 })
                ]
              })
            ]
          })
        },
        children: docChildren
      }
    ]
  });

  const buffer = await Packer.toBuffer(doc);
  fs.writeFileSync('MANUAL_DE_USUARIO_MICRODICCIONARIO.docx', buffer);
  console.log('DOCX written to MANUAL_DE_USUARIO_MICRODICCIONARIO.docx (Size: ' + buffer.length + ' bytes)');
}

// -------------------------------------------------------------
// 2. GENERATE PDF FILE
// -------------------------------------------------------------
function generatePdf() {
  return new Promise((resolve, reject) => {
    const doc = new PDFDocument({
      size: 'A4',
      margins: { top: 50, bottom: 50, left: 50, right: 50 },
      bufferPages: true
    });

    const stream = fs.createWriteStream('MANUAL_DE_USUARIO_MICRODICCIONARIO.pdf');
    doc.pipe(stream);

    const primaryColor = '#002B49';
    const secondaryColor = '#B38E32';
    const textColor = '#1F2937';
    const mutedColor = '#64748B';

    // Cover Page
    if (logoBuffer) {
      doc.image(logoBuffer, (doc.page.width - 120) / 2, 70, { width: 120 });
      doc.y = 210;
    } else {
      doc.y = 120;
    }

    doc.fillColor(primaryColor).fontSize(16).font('Helvetica-Bold')
       .text('UNIVERSIDAD MARIANO GÁLVEZ DE GUATEMALA', { align: 'center' });
    doc.moveDown(0.4);
    doc.fillColor(secondaryColor).fontSize(12).font('Helvetica-Oblique')
       .text('Facultad de Ciencias Económicas • Cátedra de Microeconomía', { align: 'center' });
    doc.moveDown(2);

    doc.fillColor(primaryColor).fontSize(26).font('Helvetica-Bold')
       .text('MANUAL DE USUARIO OFICIAL', { align: 'center' });
    doc.moveDown(0.5);
    doc.fillColor(secondaryColor).fontSize(16).font('Helvetica-Bold')
       .text('Aplicación Móvil: MicroDiccionario de Economía', { align: 'center' });
    doc.moveDown(0.5);
    doc.fillColor(mutedColor).fontSize(11).font('Helvetica')
       .text('Guía Didáctica y Operativa para Estudiantes y Docentes', { align: 'center' });
    doc.moveDown(3);

    // Callout box on cover
    const boxX = 60;
    const boxY = doc.y;
    const boxW = doc.page.width - 120;
    const boxH = 90;
    doc.rect(boxX, boxY, boxW, boxH).fill('#F0F4F8');
    doc.rect(boxX, boxY, 4, boxH).fill(secondaryColor);
    doc.fillColor(primaryColor).fontSize(11).font('Helvetica-Bold')
       .text('ESPECIFICACIONES DEL SISTEMA', boxX + 16, boxY + 12);
    doc.fillColor(textColor).fontSize(9.5).font('Helvetica')
       .text('Versión de Software: 1.0.0 Oficial (2026)\nBase de Datos: 250 Conceptos Económicos Predefinidos\nPlataforma de Ejecución: Android 8.0+ (Material Design 3)\nIntegración: Listo para importar y abrir en Google Drive / Google Docs', boxX + 16, boxY + 28, { width: boxW - 32, lineGap: 3 });

    doc.addPage();

    function addSectionTitle(num, title) {
      doc.moveDown(1.5);
      doc.fillColor(primaryColor).fontSize(14).font('Helvetica-Bold')
         .text(`${num}. ${title}`);
      doc.strokeColor(primaryColor).lineWidth(1)
         .moveTo(50, doc.y + 2).lineTo(doc.page.width - 50, doc.y + 2).stroke();
      doc.moveDown(0.6);
    }

    function addSubTitle(subTitle) {
      doc.moveDown(0.8);
      doc.fillColor(secondaryColor).fontSize(11.5).font('Helvetica-Bold')
         .text(subTitle);
      doc.moveDown(0.3);
    }

    function addBody(text) {
      doc.fillColor(textColor).fontSize(10).font('Helvetica')
         .text(text, { align: 'justify', lineGap: 3 });
      doc.moveDown(0.5);
    }

    function addBullet(label, desc) {
      doc.fillColor(primaryColor).fontSize(9.5).font('Helvetica-Bold')
         .text(`• ${label}: `, { continued: true });
      doc.fillColor(textColor).font('Helvetica')
         .text(desc, { lineGap: 2 });
      doc.moveDown(0.2);
    }

    addSectionTitle('1', 'INTRODUCCIÓN Y PROPÓSITO DEL SISTEMA');
    addBody('MicroDiccionario es una solución móvil diseñada específicamente para apoyar a los estudiantes de la Universidad Mariano Gálvez de Guatemala en la asimilación profunda de los términos y principios de la Microeconomía.');
    addBody('La aplicación elimina las barreras de búsqueda convencional ofreciendo acceso inmediato a 250 conceptos rigurosos, pronunciación en audio, ejemplos pedagógicos y un módulo dinámico de flashcards de estudio.');
    addSubTitle('1.1 Características Clave');
    addBullet('250 Términos Económicos', 'Definiciones científicas y ejemplos contextualizados.');
    addBullet('Buscador Fluido con Teclado Persistente', 'El teclado no se cierra al escribir letras consecutivas.');
    addBullet('Búsqueda por Reconocimiento de Voz', 'Dictado por voz mediante el micrófono de la barra de búsqueda.');
    addBullet('Síntesis Auditiva (Text-to-Speech)', 'Pronunciación y lectura de definiciones en voz alta.');
    addBullet('Modo Flashcards', 'Entrenamiento interactivo con autoevaluación para exámenes.');
    addBullet('Operación Offline', 'No requiere conexión activa a internet para consultar los términos.');

    addSectionTitle('2', 'ESTRUCTURA Y NAVEGACIÓN');
    addBody('La aplicación se estructura en 5 secciones accesibles permanentemente desde la barra inferior:');
    addBullet('Inicio (Home)', 'Buscador en tiempo real, Palabra del Día y catálogo dinámico.');
    addBullet('Explorar (Explore)', 'Índice alfabético completo con botones de filtrado de la A a la Z.');
    addBullet('Estudio (Flashcards)', 'Simulador de tarjetas de estudio con contador de aciertos.');
    addBullet('Favoritos (Favorites)', 'Glosario personalizado de términos marcados con estrella.');
    addBullet('Acerca de (About)', 'Información institucional y académica de la UMG.');

    addSectionTitle('3', 'MÓDULO DE INICIO (HOME) Y BÚSQUEDA');
    addSubTitle('3.1 Buscador Rápido');
    addBody('Para buscar cualquier concepto, simplemente toque la barra superior y comience a escribir. La lista se actualizará en tiempo real sin cerrar el teclado. Para borrar la búsqueda, toque el botón (X).');
    addSubTitle('3.2 Búsqueda por Voz');
    addBody('Toque el icono del micrófono en la barra de búsqueda y hable claramente (por ejemplo: "Costo fijo"). La aplicación transcribirá su voz y mostrará los resultados correspondientes.');
    addSubTitle('3.3 Palabra del Día');
    addBody('En la pantalla de inicio se destaca diariamente un concepto económico con su definición y botón de audio para escuchar la pronunciación.');

    addSectionTitle('4', 'MÓDULO DE EXPLORACIÓN Y GLOSARIO A-Z');
    addBody('En la pestaña Explorar, el usuario puede desplazarse horizontalmente por los chips de letras (A, B, C...) para filtrar los conceptos alfabéticamente. Cada tarjeta resume el término y permite acceder a la ficha completa con un toque.');

    addSectionTitle('5', 'FICHA DETALLADA DEL TÉRMINO');
    addBody('Al pulsar sobre un término, se despliega su pantalla de detalle que contiene:');
    addBullet('Definición Rigurosa', 'Explicación del concepto según la teoría económica moderna.');
    addBullet('Ejemplo Práctico', 'Caso de uso ilustrativo en mercados, empresas o toma de decisiones.');
    addBullet('Botón de Audio (TTS)', 'Reproduce oralmente la definición para facilitar el aprendizaje auditivo.');
    addBullet('Favoritos', 'Icono de estrella para guardar el concepto en la biblioteca personal.');
    addBullet('Copiar y Compartir', 'Permite transferir el contenido a documentos, chats o apuntes.');

    addSectionTitle('6', 'MODO DE ESTUDIO INTERACTIVO (FLASHCARDS)');
    addBody('Las flashcards permiten poner a prueba la retención del estudiante:');
    addBullet('Girar Tarjeta (Flip)', 'Toque la tarjeta para alternar entre el término y su significado.');
    addBullet('Botón "Lo sé"', 'Registra el término como dominado y pasa a la siguiente tarjeta.');
    addBullet('Botón "Repasar"', 'Mantiene el término pendiente para reforzarlo.');
    addBullet('Barra de Progreso', 'Muestra el porcentaje completado de la sesión de estudio.');

    addSectionTitle('7', 'BIBLIOTECA DE FAVORITOS Y SECCIÓN INSTITUCIONAL');
    addBody('La sección Favoritos concentra los términos que el estudiante considera prioritarios para repasar antes de exámenes. La sección Acerca de resume la visión académica de la Universidad Mariano Gálvez de Guatemala y los créditos docentes.');

    addSectionTitle('8', 'PREGUNTAS FRECUENTES (FAQ)');
    addBullet('¿Por qué el teclado no se cierra al escribir?', 'Se optimizó la arquitectura en Jetpack Compose para que el campo de búsqueda conserve el foco permanentemente.');
    addBullet('¿Funciona sin internet?', 'Sí, la base de datos de 250 conceptos está instalada en el teléfono.');
    addBullet('¿Cómo activo la voz?', 'Asegúrate de tener volumen multimedia y el motor de síntesis de voz de Google en español.');

    addSectionTitle('9', 'INSTRUCCIONES PARA GOOGLE DRIVE / GOOGLE DOCS');
    addBody('Este manual ha sido preparado para integrarse perfectamente con su cuenta de Google Drive:');
    addBullet('Paso 1', 'Descargue el archivo MANUAL_DE_USUARIO_MICRODICCIONARIO.docx de este proyecto.');
    addBullet('Paso 2', 'Ingrese a su Google Drive (drive.google.com) y arrastre el archivo .docx a su unidad.');
    addBullet('Paso 3', 'Haga doble clic en el archivo y elija "Abrir con Documentos de Google". Podrá editarlo, añadir notas y compartirlo.');

    const range = doc.bufferedPageRange();
    for (let i = 1; i < range.count; i++) {
      doc.switchToPage(i);
      doc.fillColor(mutedColor).fontSize(8.5).font('Helvetica')
         .text(
           `MicroDiccionario UMG • Manual de Usuario Oficial | Página ${i + 1} de ${range.count}`,
           50,
           doc.page.height - 35,
           { align: 'center', width: doc.page.width - 100 }
         );
    }

    doc.end();
    stream.on('finish', () => {
      console.log('PDF written to MANUAL_DE_USUARIO_MICRODICCIONARIO.pdf (Size: ' + fs.statSync('MANUAL_DE_USUARIO_MICRODICCIONARIO.pdf').size + ' bytes)');
      resolve();
    });
    stream.on('error', reject);
  });
}

// -------------------------------------------------------------
// 3. GENERATE RESPONSIVE HTML MANUAL
// -------------------------------------------------------------
function generateHtml() {
  const htmlContent = `<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manual de Usuario - MicroDiccionario UMG</title>
  <style>
    :root {
      --primary: #002B49;
      --primary-light: #0A436D;
      --secondary: #B38E32;
      --secondary-light: #FBF4E4;
      --bg-page: #F8FAFC;
      --bg-card: #FFFFFF;
      --text: #1E293B;
      --text-muted: #64748B;
      --border: #E2E8F0;
      --success: #15803D;
      --warning: #B45309;
    }

    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
      background-color: var(--bg-page);
      color: var(--text);
      line-height: 1.6;
      padding: 30px 15px;
    }

    .container {
      max-width: 900px;
      margin: 0 auto;
      background: var(--bg-card);
      border-radius: 16px;
      box-shadow: 0 10px 30px rgba(0,0,0,0.06);
      padding: 48px;
      border: 1px solid var(--border);
    }

    .header-banner {
      text-align: center;
      padding-bottom: 30px;
      border-bottom: 2px solid var(--primary);
      margin-bottom: 36px;
    }

    .logo {
      width: 110px;
      height: 110px;
      object-fit: contain;
      margin-bottom: 15px;
    }

    .institution {
      font-size: 1.25rem;
      font-weight: 700;
      color: var(--primary);
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }

    .faculty {
      font-size: 0.95rem;
      color: var(--secondary);
      font-style: italic;
      margin-bottom: 12px;
    }

    .doc-title {
      font-size: 2rem;
      font-weight: 800;
      color: var(--primary);
      margin-top: 10px;
    }

    .doc-subtitle {
      font-size: 1.15rem;
      color: var(--secondary);
      font-weight: 600;
    }

    .meta-box {
      background: var(--secondary-light);
      border-left: 4px solid var(--secondary);
      padding: 16px 20px;
      border-radius: 8px;
      margin: 24px 0 36px 0;
      font-size: 0.95rem;
    }

    .btn-bar {
      display: flex;
      gap: 12px;
      justify-content: center;
      margin: 20px 0 30px 0;
    }

    .btn {
      padding: 10px 20px;
      border-radius: 8px;
      text-decoration: none;
      font-weight: 600;
      font-size: 0.9rem;
      cursor: pointer;
      border: none;
      transition: all 0.2s;
    }

    .btn-primary { background: var(--primary); color: #fff; }
    .btn-primary:hover { background: var(--primary-light); }
    .btn-outline { background: transparent; border: 2px solid var(--primary); color: var(--primary); }

    h2 {
      font-size: 1.4rem;
      color: var(--primary);
      border-bottom: 2px solid #E2E8F0;
      padding-bottom: 8px;
      margin: 36px 0 16px 0;
    }

    h3 {
      font-size: 1.1rem;
      color: var(--secondary);
      margin: 20px 0 10px 0;
    }

    p { margin-bottom: 14px; }

    ul, ol {
      margin-left: 24px;
      margin-bottom: 18px;
    }

    li { margin-bottom: 8px; }
    li strong { color: var(--primary); }

    .callout {
      background: #F1F5F9;
      border-left: 4px solid var(--primary);
      padding: 14px 18px;
      border-radius: 6px;
      margin: 18px 0;
      font-size: 0.95rem;
    }

    .screen-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
      gap: 20px;
      margin: 24px 0;
    }

    .mockup-card {
      border: 1px solid var(--border);
      border-radius: 12px;
      padding: 18px;
      background: #FFFFFF;
      box-shadow: 0 4px 12px rgba(0,0,0,0.03);
    }

    .mockup-card-header {
      font-weight: 700;
      color: var(--primary);
      font-size: 1.05rem;
      margin-bottom: 8px;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .mockup-card-body {
      font-size: 0.9rem;
      color: var(--text-muted);
    }

    .step-badge {
      display: inline-block;
      background: var(--primary);
      color: white;
      font-weight: 700;
      font-size: 0.8rem;
      width: 24px;
      height: 24px;
      text-align: center;
      line-height: 24px;
      border-radius: 50%;
      margin-right: 8px;
    }

    .drive-steps {
      background: #EFF6FF;
      border: 1px solid #BFDBFE;
      border-radius: 12px;
      padding: 24px;
      margin: 28px 0;
    }

    .drive-steps h3 {
      color: #1D4ED8;
      margin-top: 0;
    }

    @media print {
      body { background: white; padding: 0; }
      .container { box-shadow: none; border: none; padding: 0; max-width: 100%; }
      .btn-bar { display: none; }
    }
  </style>
</head>
<body>

<div class="container">
  <div class="header-banner">
    <img src="app/src/main/res/drawable/logo_umg.png" alt="Logo UMG" class="logo" onerror="this.style.display='none'">
    <div class="institution">Universidad Mariano Gálvez de Guatemala</div>
    <div class="faculty">Facultad de Ciencias Económicas • Cátedra de Microeconomía</div>
    <h1 class="doc-title">MANUAL DE USUARIO OFICIAL</h1>
    <div class="doc-subtitle">MicroDiccionario de Economía para Android</div>
  </div>

  <div class="btn-bar">
    <button class="btn btn-primary" onclick="window.print()">🖨️ Imprimir / Guardar en PDF</button>
  </div>

  <div class="meta-box">
    <strong>📋 Ficha Técnica:</strong><br>
    • Versión: 1.0.0 Oficial (2026)<br>
    • Base de Datos: 250 Conceptos Económicos Predefinidos<br>
    • Plataforma: Android 8.0+ (Material Design 3)<br>
    • Compatibilidad: Archivo .docx compatible nativamente con <strong>Google Drive y Google Docs</strong>.
  </div>

  <h2>1. INTRODUCCIÓN Y PROPÓSITO</h2>
  <p><strong>MicroDiccionario</strong> es una herramienta didáctica integral concebida para estudiantes y docentes universitarios de la Universidad Mariano Gálvez de Guatemala. Su objetivo es proporcionar un acceso inmediato, confiable y científicamente estructurado a los fundamentos de la teoría económica y la microeconomía.</p>

  <div class="screen-grid">
    <div class="mockup-card">
      <div class="mockup-card-header">🔍 Búsqueda Fluida</div>
      <div class="mockup-card-body">Buscador instantáneo en vivo. El teclado se mantiene abierto mientras escribes cualquier letra, sin interrupciones.</div>
    </div>
    <div class="mockup-card">
      <div class="mockup-card-header">🎙️ Búsqueda por Voz</div>
      <div class="mockup-card-body">Dicta cualquier concepto económico usando el micrófono integrado para obtener resultados de inmediato.</div>
    </div>
    <div class="mockup-card">
      <div class="mockup-card-header">🔊 Síntesis de Voz (TTS)</div>
      <div class="mockup-card-body">Escucha la pronunciación y definición exacta de cada término en voz alta con un solo toque.</div>
    </div>
    <div class="mockup-card">
      <div class="mockup-card-header">📇 Flashcards de Estudio</div>
      <div class="mockup-card-body">Tarjetas interactivas volteables con sistema de autoevaluación ('Lo sé' / 'Repasar') para preparar exámenes.</div>
    </div>
  </div>

  <h2>2. ESTRUCTURA Y NAVEGACIÓN</h2>
  <p>La aplicación cuenta con una barra de navegación inferior fija para cambiar de sección rápidamente:</p>
  <ul>
    <li><strong>Inicio (Home):</strong> Búsqueda en vivo, Palabra del Día, acceso rápido y escudo institucional UMG.</li>
    <li><strong>Explorar (Explore):</strong> Catálogo alfabético completo con botones de letras (A-Z) para filtrar los 250 términos.</li>
    <li><strong>Flashcards (Estudio):</strong> Modo de entrenamiento interactivo para memorización activa.</li>
    <li><strong>Favoritos (Favorites):</strong> Colección personalizada de conceptos marcados por el estudiante.</li>
    <li><strong>Acerca de (About):</strong> Identidad académica, objetivos y créditos del proyecto de la UMG.</li>
  </ul>

  <h2>3. GUÍA PASO A PASO POR MÓDULOS</h2>

  <h3>3.1 Módulo de Inicio y Buscador Rápido</h3>
  <ol>
    <li>Toca el campo de búsqueda en la parte superior.</li>
    <li>Escribe cualquier término (ej. <em>"Costo"</em> o <em>"Elasticidad"</em>). La lista se actualiza instantáneamente sin cerrar el teclado.</li>
    <li>Para usar reconocimiento de voz, pulsa el <strong>icono de micrófono</strong> y habla claro.</li>
    <li>Para borrar la búsqueda y volver a la vista inicial, pulsa el botón <strong>(X)</strong>.</li>
  </ol>

  <h3>3.2 Tarjeta de la Palabra del Día</h3>
  <p>En el Inicio se muestra diariamente un concepto económico aleatorio con su definición y un botón de audio directo para escuchar la pronunciación.</p>

  <h3>3.3 Módulo Explorar (Glosario A-Z)</h3>
  <p>Desplázate horizontalmente por los chips de letras (A, B, C...) para consultar únicamente los términos de una inicial específica. Cada tarjeta muestra la definición resumida y permite ingresar al detalle completo.</p>

  <h3>3.4 Ficha Detallada de Concepto</h3>
  <p>Al tocar cualquier término, se abre su pantalla completa donde puedes:</p>
  <ul>
    <li>Leer la definición teórica completa.</li>
    <li>Revisar el <strong>ejemplo práctico</strong> de aplicación en el mercado real.</li>
    <li>Pulsar el <strong>botón de audio</strong> para escuchar la lectura automática.</li>
    <li>Tocar la <strong>estrella</strong> para añadirlo a tus Favoritos.</li>
    <li>Copiar la definición al portapapeles para citarla en tus tareas.</li>
  </ul>

  <h3>3.5 Modo Flashcards de Estudio</h3>
  <ol>
    <li>Ingresa a la pestaña <strong>Flashcards</strong>.</li>
    <li>Lee el término en el frente de la tarjeta y reflexiona su definición mentalmente.</li>
    <li>Toca la tarjeta para <strong>girarla</strong> y corroborar la definición en el reverso.</li>
    <li>Si la acertaste, pulsa <strong>"Lo sé" (Verde)</strong>; si necesitas reforzarla, pulsa <strong>"Repasar" (Ámbar)</strong>.</li>
    <li>La barra de progreso superior indicará tu avance en la sesión de estudio.</li>
  </ol>

  <div class="drive-steps">
    <h3>☁️ CÓMO GUARDAR Y ABRIR ESTE MANUAL EN GOOGLE DRIVE</h3>
    <p>Para tener este manual en tu Google Drive y abrirlo como Google Docs:</p>
    <p><span class="step-badge">1</span> Descarga el archivo <code>MANUAL_DE_USUARIO_MICRODICCIONARIO.docx</code> generado en la raíz del proyecto.</p>
    <p><span class="step-badge">2</span> Abre tu navegador e ingresa a <strong>drive.google.com</strong> con tu cuenta de Google.</p>
    <p><span class="step-badge">3</span> Arrastra el archivo <code>.docx</code> a tu unidad de Google Drive (o haz clic en <em>"+ Nuevo" &gt; "Subir archivo"</em>).</p>
    <p><span class="step-badge">4</span> Haz doble clic sobre el archivo en tu Drive y selecciona <strong>"Abrir con Documentos de Google"</strong>. ¡Listo! Tendrás el documento completo, editable, con portadas y encabezados oficiales listo para compartir o imprimir.</p>
  </div>

  <h2>4. PREGUNTAS FRECUENTES (FAQ)</h2>
  <div class="callout">
    <strong>¿Por qué el buscador no me cierra el teclado al escribir?</strong><br>
    Se implementó una corrección en Jetpack Compose para que la barra de búsqueda sea persistente y no se destruya al filtrar resultados. Así puedes escribir de forma continua.
  </div>
  <div class="callout">
    <strong>¿Puedo usar la aplicación sin conexión a internet?</strong><br>
    Sí. Todos los 250 conceptos económicos y el modo flashcards están almacenados localmente en la base de datos interna de la app.
  </div>
  <div class="callout">
    <strong>¿Cómo activo la síntesis de voz si no se escucha?</strong><br>
    Sube el volumen multimedia de tu teléfono Android y comprueba que en <em>Ajustes &gt; Accesibilidad / Sistema &gt; Texto a voz</em> tengas instalado el idioma Español.
  </div>

  <div style="text-align: center; margin-top: 40px; color: var(--text-muted); font-size: 0.85rem; border-top: 1px solid var(--border); padding-top: 20px;">
    Universidad Mariano Gálvez de Guatemala • Facultad de Ciencias Económicas • MicroDiccionario v1.0
  </div>
</div>

</body>
</html>
`;

  fs.writeFileSync('MANUAL_DE_USUARIO_MICRODICCIONARIO.html', htmlContent);
  console.log('HTML written to MANUAL_DE_USUARIO_MICRODICCIONARIO.html');
}

async function main() {
  await generateDocx();
  await generatePdf();
  generateHtml();
  console.log('All formats generated successfully!');
}

main().catch(err => {
  console.error('Error generating manuals:', err);
  process.exit(1);
});
