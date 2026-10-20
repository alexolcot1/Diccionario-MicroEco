import os
import sys

# Ensure current dir in path
sys.path.insert(0, ".")
from pdf_builder import PDFBuilder

def generate():
    pdf = PDFBuilder("manual_de_usuario.pdf")
    pdf.load_image("logo_umg_opt.jpg")

    # ==========================================
    # PÁGINA 1: PORTADA OFICIAL INSTITUCIONAL
    # ==========================================
    p1 = pdf.add_page()
    # Decorative top header band
    p1.rect(0, 800, 595.28, 42, fill=(0.05, 0.22, 0.52))
    p1.rect(0, 794, 595.28, 6, fill=(0.85, 0.65, 0.13)) # Gold accent
    
    # Institution text
    p1.center_text(760, "UNIVERSIDAD MARIANO G\301LVEZ DE GUATEMALA", font="/F1", size=14, color=(0.05, 0.22, 0.52))
    p1.center_text(742, "FACULTAD DE INGENIER\315A EN SISTEMAS DE INFORMACI\323N", font="/F1", size=11, color=(0.25, 0.35, 0.45))
    p1.center_text(726, "SEDE REGIONAL DE CHIMALTENANGO", font="/F2", size=10, color=(0.35, 0.45, 0.55))
    p1.center_text(710, "C\341tedra de Microeconom\355a (C\363digo 016)", font="/F3", size=10, color=(0.4, 0.5, 0.6))

    # Divider
    p1.line(80, 696, 515, 696, stroke=(0.82, 0.86, 0.92), line_width=1.5)

    # UMG Logo
    p1.draw_image(227, 545, 140, 140)

    # Title Card
    p1.rounded_rect(50, 410, 495.28, 115, r=12, fill=(0.95, 0.97, 1.0), stroke=(0.75, 0.83, 0.94), line_width=1.5)
    p1.center_text(490, "MANUAL DE USUARIO OFICIAL", font="/F1", size=18, color=(0.85, 0.45, 0.05))
    p1.center_text(462, "MicroDiccionario de Microeconom\355a", font="/F1", size=22, color=(0.05, 0.22, 0.52))
    p1.center_text(438, "Gu\355a Did\341ctica Interactiva y Aplicaci\363n M\363vil de Estudio", font="/F2", size=12, color=(0.25, 0.35, 0.45))
    p1.center_text(420, "Glosario Completo de 250 T\351rminos \225 Reconocimiento y S\355ntesis de Voz \225 Tarjetas Flashcards", font="/F3", size=9, color=(0.4, 0.45, 0.5))

    # Authors Box
    p1.rounded_rect(65, 120, 465.28, 260, r=10, fill=(1, 1, 1), stroke=(0.82, 0.86, 0.92), line_width=1)
    p1.rounded_rect(65, 345, 465.28, 35, r=10, fill=(0.05, 0.22, 0.52))
    p1.text(85, 358, "EQUIPO DE DESARROLLO E INVESTIGACI\323N", font="/F1", size=11, color=(1, 1, 1))
    p1.text(390, 358, "4to Semestre - 2026", font="/F2", size=10, color=(0.85, 0.92, 1))

    members = [
        ("Kevin Alexander Olcot Ejcalon", "1990-25-20016", "L\355der de Proyecto & Arquitectura"),
        ("Pablo Alexander Hern\341ndez Mucia", "1990-25-17528", "Desarrollo Android & Base de Datos"),
        ("F\341tima Alejandra Estrada Estrada", "1990-25-16576", "Dise\361o UI/UX & Tarjetas Flashcards"),
        ("Lesli Iliana Cali Tuyuc", "1990-24-17472", "Contenido Acad\351mico & Glosario"),
        ("Dina Raquel Aju Xicay", "1990-22-15361", "Control de Calidad & Pruebas")
    ]

    my = 315
    for name, carne, role in members:
        # Icon bullet
        p1.rounded_rect(85, my - 2, 8, 8, r=4, fill=(0.05, 0.28, 0.63))
        p1.text(102, my, name, font="/F1", size=10, color=(0.1, 0.15, 0.2))
        p1.text(320, my, f"Carn\351: {carne}", font="/F4", size=9.5, color=(0.05, 0.28, 0.63))
        p1.text(102, my - 13, role, font="/F3", size=8.5, color=(0.45, 0.5, 0.55))
        p1.line(85, my - 20, 510, my - 20, stroke=(0.92, 0.94, 0.96), line_width=0.6)
        my -= 40

    # Bottom footer note
    p1.center_text(60, "Guatemala, Centroam\351rica \225 Edici\363n Oficial 2026", font="/F1", size=10, color=(0.3, 0.35, 0.4))
    p1.center_text(45, "Documento t\351cnico de operaci\363n para estudiantes y docentes universitarios.", font="/F3", size=8.5, color=(0.5, 0.55, 0.6))

    # ==========================================
    # PÁGINA 2: ÍNDICE & INTRODUCCIÓN GENERAL
    # ==========================================
    p2 = pdf.add_page()
    p2.draw_header("MicroDiccionario • \315ndice & Introducci\363n")
    p2.draw_footer(2)

    y = p2.draw_section_title(40, 770, "01", "\315NDICE GENERAL DEL MANUAL")

    # Table of contents card
    p2.draw_card_box(40, y - 180, 515, 180, title="\315NDICE SISTEM\301TICO DE CONTENIDOS")
    toc_items = [
        ("Cap\355tulo 1: Introducci\363n y Requisitos T\351cnicos del Software", "P\341g. 2"),
        ("Cap\355tulo 2: Arquitectura del Sistema y Navegaci\363n General", "P\341g. 3"),
        ("Cap\355tulo 3: Pantalla Principal (Inicio) y B\372squeda R\341pida por Voz", "P\341g. 4"),
        ("Cap\355tulo 4: M\363dulo de Exploraci\363n Alfab\351tica (Glosario A - Z)", "P\341g. 5"),
        ("Cap\355tulo 5: Ficha T\351cnica de Detalle y S\355ntesis de Voz (TTS)", "P\341g. 5"),
        ("Cap\355tulo 6: M\363dulo de Repaso Activo con Tarjetas Flashcards 3D", "P\341g. 6"),
        ("Cap\355tulo 7: Gesti\363n de Favoritos y Registro de Nuevos T\351rminos", "P\341g. 7"),
        ("Cap\355tulo 8: Resoluci\363n de Problemas T\351cnicos y Preguntas Frecuentes", "P\341g. 8"),
        ("Cap\355tulo 9: Ficha T\351cnica Institucional y Licencia Acad\351mica", "P\341g. 8"),
    ]

    ty = y - 35
    for title, page_str in toc_items:
        p2.text(56, ty, "\225", font="/F1", size=9, color=(0.05, 0.28, 0.63))
        p2.text(68, ty, title, font="/F2", size=9, color=(0.15, 0.2, 0.25))
        p2.line(380, ty + 2, 490, ty + 2, stroke=(0.8, 0.85, 0.9), line_width=0.6)
        p2.text(500, ty, page_str, font="/F1", size=9, color=(0.05, 0.28, 0.63))
        ty -= 16

    y = p2.draw_section_title(40, y - 210, "02", "INTRODUCCI\323N Y PROP\323SITO DEL SISTEMA")

    p2.text(40, y, "\277Qu\351 es MicroDiccionario UMG?", font="/F1", size=11, color=(0.05, 0.28, 0.63))
    y -= 16
    intro_p1 = "MicroDiccionario es una aplicaci\363n m\363vil nativa para Android dise\361ada con tecnolog\355a moderna Jetpack Compose, concebida para brindar a los estudiantes de Ingenier\355a en Sistemas una herramienta de consulta inmediata, repaso din\341mico y preparaci\363n rigurosa para los ex\341menes de la c\341tedra de Microeconom\355a."
    for line in [intro_p1[:88], intro_p1[88:175], intro_p1[175:]]:
        p2.text(40, y, line, font="/F2", size=9.5, color=(0.2, 0.25, 0.3))
        y -= 14

    y -= 8
    p2.text(40, y, "Objetivos Principales:", font="/F1", size=10, color=(0.05, 0.28, 0.63))
    y -= 14

    objectives = [
        ("Centralizaci\363n de Conocimiento:", "Proveer un glosario unificado de 250 t\351rminos econ\363micos clave (A-Z)."),
        ("Comprensi\363n Auditiva y Fon\351tica:", "Integrar un motor TTS para pronunciaci\363n natural en espa\361ol latino."),
        ("B\372squeda R\341pida Manos Libres:", "Implementar reconocimiento de voz para consultas instant\341neas."),
        ("Pedagog\355a Activa (Flashcards):", "Habilitar tarjetas de estudio interactivas para reforzar conceptos clave."),
        ("Disponibilidad Total Fuera de L\355nea:", "Base de datos Room SQLite local que no requiere internet.")
    ]

    for bold_prefix, desc in objectives:
        p2.rounded_rect(42, y - 1, 6, 6, r=3, fill=(0.85, 0.45, 0.05))
        p2.text(54, y, bold_prefix, font="/F1", size=9, color=(0.1, 0.15, 0.2))
        p2.text(220, y, desc, font="/F2", size=9, color=(0.25, 0.3, 0.35))
        y -= 15

    y -= 12
    # Requirements card
    p2.draw_card_box(40, y - 95, 515, 95, title="REQUISITOS DEL SISTEMA Y PERMISOS")
    p2.text(55, y - 35, "\225 Sistema Operativo:", font="/F1", size=9, color=(0.15, 0.2, 0.25))
    p2.text(170, y - 35, "Android 7.0 (Nougat, API 24) o superior. Optimizado para Android 14/15/16.", font="/F2", size=9, color=(0.3, 0.35, 0.4))
    p2.text(55, y - 52, "\225 Memoria RAM y Disco:", font="/F1", size=9, color=(0.15, 0.2, 0.25))
    p2.text(170, y - 52, "2 GB de memoria RAM m\355nima (3 GB recomendado). Espacio libre: 35 MB.", font="/F2", size=9, color=(0.3, 0.35, 0.4))
    p2.text(55, y - 69, "\225 Permiso RECORD_AUDIO:", font="/F1", size=9, color=(0.15, 0.2, 0.25))
    p2.text(170, y - 69, "Requerido \372nicamente para la captura de consultas por voz (Google Voice Recognizer).", font="/F2", size=9, color=(0.3, 0.35, 0.4))
    p2.text(55, y - 86, "\225 Conexi\363n a Internet:", font="/F1", size=9, color=(0.15, 0.2, 0.25))
    p2.text(170, y - 86, "No obligatoria. El 100% del glosario y flashcards operan de forma local.", font="/F2", size=9, color=(0.1, 0.55, 0.2))


    # ==========================================
    # PÁGINA 3: ARQUITECTURA & NAVEGACIÓN
    # ==========================================
    p3 = pdf.add_page()
    p3.draw_header("MicroDiccionario • Arquitectura & Navegaci\363n")
    p3.draw_footer(3)

    y = p3.draw_section_title(40, 770, "03", "ARQUITECTURA DE LA APLICACI\323N Y PESTA\321AS")

    # Phone Mockup left side
    mockup_items = [
        {"type": "banner", "title": "MicroDiccionario", "sub": "Gu\355a Did\341ctica UMG", "h": 28},
        {"type": "search", "text": "B\372squeda R\341pida..."},
        {"type": "banner", "title": "[Escudo Oficial UMG]", "sub": "Universidad Mariano G\341lvez", "h": 36},
        {"type": "card", "title": "Palabra del D\355a", "desc": "Costo de Oportunidad: Mejor alternativa a la que se renuncia.", "h": 46},
        {"type": "card", "title": "Glosario A - Z (250 t\351rminos)", "desc": "Toca para explorar todos los conceptos ordenados.", "h": 40},
    ]
    p3.draw_phone_mockup(45, 330, 210, 390, "MicroDiccionario UMG", mockup_items)

    # Explanation cards right side
    rx = 275
    rw = 280
    
    p3.draw_card_box(rx, 630, rw, 90, title="1. PESTA\321A INICIO (HOME)")
    p3.text(rx + 12, 690, "Centro de control de la aplicaci\363n:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p3.text(rx + 12, 676, "\225 Acceso instant\341neo al buscador con micr\363fono.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 663, "\225 Escudo oficial y Palabra del D\355a interactiva.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 650, "\225 Men\372 flotante de acciones r\341pidas (Kebab).", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 638, "\225 Los resultados de b\372squeda aparecen en tiempo real.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))

    p3.draw_card_box(rx, 525, rw, 95, title="2. PESTA\321A EXPLORAR (A - Z)")
    p3.text(rx + 12, 585, "Directorio alfab\351tico exhaustivo:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p3.text(rx + 12, 571, "\225 Lista completa de 250 conceptos macro y microecon\363micos.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 558, "\225 Filtro de b\372squeda por letra y t\351rmino.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 545, "\225 Bot\363n de favoritos directo en cada tarjeta.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 533, "\225 Toca cualquier t\351rmino para abrir su ficha completa.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))

    p3.draw_card_box(rx, 420, rw, 95, title="3. PESTA\321A REPASO (FLASHCARDS)")
    p3.text(rx + 12, 480, "M\363dulo did\341ctico de memorizaci\363n activa:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p3.text(rx + 12, 466, "\225 Tarjetas de estudio interactivas con animaci\363n 3D.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 453, "\225 Cara frontal: T\351rmino y categor\355a alfab\351tica.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 440, "\225 Cara posterior: Definici\363n completa y ejemplo.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 428, "\225 Barra de avance num\351rico y selector de favoritos.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))

    p3.draw_card_box(rx, 315, rw, 95, title="4. PESTA\321A ACERCA DE (INFO)")
    p3.text(rx + 12, 375, "Identidad institucional y cr\351ditos:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p3.text(rx + 12, 361, "\225 Informaci\363n oficial de la Universidad Mariano G\341lvez.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 348, "\225 Detalle del grupo de 5 estudiantes desarrolladores.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 335, "\225 Marcas de agua del escudo UMG en fondo trasl\372cido.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p3.text(rx + 12, 323, "\225 Versi\363n de la app, a\361o lectivo y curso asignado.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))

    # Bottom guidance table
    y = 280
    p3.draw_section_title(40, y, "04", "CONVENCIONES VISUALES Y BOTONES COMUNES")
    
    icons_guide = [
        ("Lupa / Teclado:", "B\372squeda instant\341nea de texto en todo el vocabulario."),
        ("Micr\363fono [Voz]:", "Activa el reconocimiento por voz para dictar t\351rminos."),
        ("Parlante [Audio]:", "Pronuncia el t\351rmino en voz alta con acento latino natural."),
        ("Marcador / Estrella:", "Guarda el t\351rmino en la lista de Favoritos para repaso r\341pido."),
        ("Bot\363n Voltear (3D):", "Gira la tarjeta flashcard para revelar su definici\363n completa."),
        ("Flechas [<] [>]:", "Navegan secuencialmente entre tarjetas de estudio.")
    ]
    
    gy = y - 35
    for i, (icn, desc) in enumerate(icons_guide):
        col_x = 45 if i < 3 else 300
        row_y = gy - (i % 3) * 20
        p3.rounded_rect(col_x, row_y - 2, 8, 8, r=2, fill=(0.05, 0.28, 0.63))
        p3.text(col_x + 14, row_y, icn, font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
        p3.text(col_x + 115, row_y, desc, font="/F2", size=8, color=(0.3, 0.35, 0.4))


    # ==========================================
    # PÁGINA 4: PANTALLA PRINCIPAL & BÚSQUEDA
    # ==========================================
    p4 = pdf.add_page()
    p4.draw_header("MicroDiccionario • Pantalla Inicio & B\372squeda")
    p4.draw_footer(4)

    y = p4.draw_section_title(40, 770, "05", "PANTALLA PRINCIPAL Y SISTEMA DE B\332SQUEDA")

    # Phone mockup of home screen searching
    mockup_home_search = [
        {"type": "banner", "title": "MicroDiccionario", "sub": "Gu\355a Did\341ctica UMG", "h": 26},
        {"type": "search", "text": "Elasticidad"},
        {"type": "card", "title": "Elasticidad", "desc": "Medida de la sensibilidad de una variable ante cambios.", "h": 42},
        {"type": "card", "title": "Elasticidad cruzada", "desc": "Cambio en la demanda al variar el precio de otro bien.", "h": 42},
        {"type": "card", "title": "Elasticidad precio demanda", "desc": "Respuesta de la cantidad demandada ante el precio.", "h": 42},
    ]
    p4.draw_phone_mockup(45, 330, 210, 390, "Inicio - B\372squeda Activa", mockup_home_search)

    # Search feature steps
    rx = 275
    rw = 280

    p4.draw_card_box(rx, 580, rw, 140, title="SISTEMA DE B\332SQUEDA PERSISTENTE")
    p4.text(rx + 12, 685, "1. B\372squeda por Texto Sin Interrupciones:", font="/F1", size=8.5, color=(0.05, 0.28, 0.63))
    p4.text(rx + 12, 672, "Escribe en la barra superior. La aplicaci\363n mantiene el foco", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 660, "y el teclado abierto en todo momento sin cerrarse al escribir.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 645, "2. Filtrado Inteligente Multicampo:", font="/F1", size=8.5, color=(0.05, 0.28, 0.63))
    p4.text(rx + 12, 632, "Busca coincidencias no solo en el t\351rmino, sino tambi\351n dentro", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 620, "del significado, resumen y categor\355a econ\363mica.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 605, "3. Bot\363n de Limpieza R\341pida [X]:", font="/F1", size=8.5, color=(0.05, 0.28, 0.63))
    p4.text(rx + 12, 592, "Aparece autom\341ticamente al escribir. T\363calo para restaurar.", font="/F2", size=8, color=(0.2, 0.25, 0.3))

    p4.draw_card_box(rx, 430, rw, 140, title="B\332SQUEDA POR VOZ (SPEECH-TO-TEXT)")
    p4.text(rx + 12, 535, "\277C\363mo utilizar el micr\363fono de voz?", font="/F1", size=8.5, color=(0.85, 0.45, 0.05))
    p4.text(rx + 12, 521, "Paso 1: Toca el icono de micr\363fono en la barra de b\372squeda.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 508, "Paso 2: Concede el permiso de audio si es la primera vez.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 495, "Paso 3: Pronuncia claramente el concepto (ej. 'Monopolio',", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 482, "'Costo marginal', 'Bien p\372blico', 'Demanda el\341stica').", font="/F3", size=8, color=(0.05, 0.28, 0.63))
    p4.text(rx + 12, 469, "Paso 4: El motor de Google procesa la voz y filtra la lista", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 456, "de t\351rminos al instante sin que tengas que teclear nada.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p4.text(rx + 12, 442, "Nota: El reconocimiento est\341 configurado para espa\361ol latino.", font="/F1", size=7.5, color=(0.1, 0.55, 0.2))

    p4.draw_card_box(rx, 315, rw, 105, title="PALABRA DEL D\315A & MEN\332 KEBAB")
    p4.text(rx + 12, 388, "\225 Palabra del D\355a:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p4.text(rx + 12, 375, "Selecciona una palabra autom\341tica por calendario.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p4.text(rx + 12, 362, "Usa el bot\363n de aleatorio (shuffle) para descubrir nuevos temas.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p4.text(rx + 12, 348, "\225 Men\372 Flotante Kebab:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p4.text(rx + 12, 335, "Ubicado en la esquina inferior. Permite abrir r\341pidamente", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p4.text(rx + 12, 323, "Favoritos o registrar un nuevo t\351rmino personalizado.", font="/F2", size=8, color=(0.3, 0.35, 0.4))

    # Step by step box at bottom
    y = 280
    p4.draw_section_title(40, y, "06", "PASO A PASO: CONSULTA R\301PIDA EN 3 PASOS")

    steps = [
        ("PASO 1", "Abre la app e ingresa la palabra", "Escribe en el campo o presiona el icono de micr\363fono para dictar el t\351rmino."),
        ("PASO 2", "Explora la lista din\341mica", "Los resultados se actualizan letra a letra mostrando coincidencia y resumen."),
        ("PASO 3", "Abre la ficha completa", "Toca la tarjeta para escuchar su pronunciaci\363n y ver ejemplos de mercado.")
    ]

    sx = 45
    sw = 160
    for step_num, title_s, desc_s in steps:
        p4.draw_card_box(sx, y - 105, sw, 80, title=step_num)
        p4.text(sx + 10, y - 55, title_s, font="/F1", size=8, color=(0.05, 0.28, 0.63))
        p4.text(sx + 10, y - 70, desc_s[:28], font="/F2", size=7.5, color=(0.3, 0.35, 0.4))
        p4.text(sx + 10, y - 82, desc_s[28:56], font="/F2", size=7.5, color=(0.3, 0.35, 0.4))
        p4.text(sx + 10, y - 94, desc_s[56:], font="/F2", size=7.5, color=(0.3, 0.35, 0.4))
        sx += 175


    # ==========================================
    # PÁGINA 5: GLOSARIO COMPLETO & FICHA DETALLE
    # ==========================================
    p5 = pdf.add_page()
    p5.draw_header("MicroDiccionario • Glosario & Detalle")
    p5.draw_footer(5)

    y = p5.draw_section_title(40, 770, "07", "GLOSARIO ALFAB\311TICO (250 T\311RMINOS) Y FICHA T\311CNICA")

    # Phone mockup of term detail
    mockup_detail = [
        {"type": "banner", "title": "[<] Detalle del T\351rmino", "sub": "Microeconom\355a", "h": 26},
        {"type": "card", "title": "Costo de Oportunidad", "desc": "Categor\355a: Letra C  |  Ficha Oficial", "h": 36},
        {"type": "banner", "title": "[Audio] Pronunciar Voz", "sub": "S\355ntesis TTS en Espa\361ol Latino", "h": 26},
        {"type": "card", "title": "Definici\363n Rigurosa", "desc": "Valor de la mejor alternativa a la que se renuncia al tomar una decisi\363n.", "h": 46},
        {"type": "card", "title": "Ejemplo en el Mercado", "desc": "Un estudiante decide dedicar 4 hrs a programar en vez de trabajar por Q150.", "h": 46},
    ]
    p5.draw_phone_mockup(45, 330, 210, 390, "Ficha T\351cnica de Detalle", mockup_detail)

    # Explanation cards right side
    rx = 275
    rw = 280

    p5.draw_card_box(rx, 560, rw, 160, title="ESTRUCTURA DE CADA T\311RMINO")
    p5.text(rx + 12, 680, "Cada una de las 250 palabras incluye:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p5.text(rx + 12, 664, "\225 T\351rmino Oficial:", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p5.text(rx + 95, 664, "Nombre estandarizado de la c\341tedra.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p5.text(rx + 12, 649, "\225 Categor\355a Alfab\351tica:", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p5.text(rx + 115, 649, "Secci\363n A a la U.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p5.text(rx + 12, 634, "\225 Significado Completo:", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p5.text(rx + 12, 622, "Explicaci\363n conceptual rigurosa y directa.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p5.text(rx + 12, 607, "\225 Ejemplo Pr\341ctico de Mercado:", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p5.text(rx + 12, 595, "Contexto de aplicaci\363n en econom\355a real y empresas.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p5.text(rx + 12, 580, "\225 Bibliograf\355a:", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p5.text(rx + 80, 580, "Referencia institucional oficial UMG.", font="/F2", size=8, color=(0.3, 0.35, 0.4))

    p5.draw_card_box(rx, 410, rw, 140, title="S\315NTESIS DE VOZ Y PRONUNCIACI\323N (TTS)")
    p5.text(rx + 12, 515, "Motor Text-to-Speech de Alta Fidelidad:", font="/F1", size=8.5, color=(0.85, 0.45, 0.05))
    p5.text(rx + 12, 501, "\225 Presiona el bot\363n flotante de audio con icono de bocina.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p5.text(rx + 12, 488, "\225 La app lee en voz alta el t\351rmino y su definici\363n.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p5.text(rx + 12, 475, "\225 Velocidad optimizada a 0.92x para m\341xima articulaci\363n.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p5.text(rx + 12, 462, "\225 Prioriza voces naturales neuronales en espa\361ol latino.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p5.text(rx + 12, 449, "\225 Ideal para estudiar mientras caminas o viajas.", font="/F3", size=8, color=(0.1, 0.55, 0.2))
    p5.text(rx + 12, 436, "Compatible con aud\355fonos Bluetooth y altavoz.", font="/F2", size=7.5, color=(0.4, 0.45, 0.5))

    p5.draw_card_box(rx, 315, rw, 85, title="BOT\323N DE FAVORITOS & NOTAS")
    p5.text(rx + 12, 368, "\225 Marcador de Favorito:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p5.text(rx + 12, 355, "Toca el icono en la barra superior para a\361adir el t\351rmino", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p5.text(rx + 12, 342, "a tu lista prioritaria de estudio.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p5.text(rx + 12, 329, "Se sincroniza de inmediato con las tarjetas flashcards.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))

    # Bottom overview table
    y = 280
    p5.draw_section_title(40, y, "08", "MUESTRA DE T\311RMINOS INCLUIDOS EN EL GLOSARIO")

    terms_sample = [
        ("Abundancia", "Existencia de una gran cantidad de bienes o recursos."),
        ("Beneficio marginal", "Aumento del beneficio total al consumir una unidad adicional."),
        ("Costo de oportunidad", "Valor de la mejor alternativa a la que se renuncia."),
        ("Demanda el\341stica", "Cantidad responde proporcionalmente m\341s que el precio."),
        ("Elasticidad precio", "Medida de la respuesta de la cantidad ante el precio."),
        ("Monopolio", "Estructura de mercado con un solo vendedor sin sustitutos."),
        ("Tasa marginal sustituci\363n", "Cantidad de un bien a sacrificar por otro."),
        ("Utilidad", "Medida de la satisfacci\363n al consumir bienes y servicios.")
    ]

    ty = y - 35
    for t_name, t_def in terms_sample:
        p5.rounded_rect(45, ty - 1, 6, 6, r=3, fill=(0.05, 0.28, 0.63))
        p5.text(56, ty, t_name, font="/F1", size=8.5, color=(0.05, 0.28, 0.63))
        p5.text(190, ty, t_def, font="/F2", size=8, color=(0.25, 0.3, 0.35))
        ty -= 14


    # ==========================================
    # PÁGINA 6: MÓDULO DE REPASO CON FLASHCARDS
    # ==========================================
    p6 = pdf.add_page()
    p6.draw_header("MicroDiccionario • M\363dulo Flashcards 3D")
    p6.draw_footer(6)

    y = p6.draw_section_title(40, 770, "09", "M\323DULO DE REPASO ACTIVO CON FLASHCARDS 3D")

    # Phone mockup of flashcards
    mockup_flash = [
        {"type": "banner", "title": "Tarjetas de Repaso", "sub": "Tarjeta 14 de 250", "h": 26},
        {"type": "banner", "title": "[Filtro] Todas (250) | Favoritas (12)", "sub": "Modo Activo", "h": 22},
        {"type": "card", "title": "Costo Marginal", "desc": "\277Cu\341l es la definici\363n?\n(Toca la tarjeta para voltear 3D)", "h": 65},
        {"type": "banner", "title": "[Voltear Tarjeta]  [Audio]", "sub": "Girar para ver significado", "h": 24},
        {"type": "banner", "title": "[< Anterior]   [Siguiente >]", "sub": "Navegaci\363n entre conceptos", "h": 24},
    ]
    p6.draw_phone_mockup(45, 330, 210, 390, "M\363dulo de Flashcards", mockup_flash)

    # Explanation cards right side
    rx = 275
    rw = 280

    p6.draw_card_box(rx, 570, rw, 150, title="METODOLOG\315A DE ESTUDIO ACTIVO (ACTIVE RECALL)")
    p6.text(rx + 12, 685, "\277Por qu\351 estudiar con Flashcards?", font="/F1", size=8.5, color=(0.85, 0.45, 0.05))
    p6.text(rx + 12, 672, "El m\351todo de recuperaci\363n activa ha demostrado cient\355ficamente", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p6.text(rx + 12, 660, "mejorar la retenci\363n en ex\341menes hasta un 300% en comparaci\363n", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p6.text(rx + 12, 648, "con la lectura pasiva tradicional.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p6.text(rx + 12, 633, "1. Mira el t\351rmino e intenta recordar su significado.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p6.text(rx + 12, 620, "2. Toca la tarjeta: gira con animaci\363n tridimensional.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p6.text(rx + 12, 607, "3. Comprueba tu respuesta y lee el ejemplo.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p6.text(rx + 12, 594, "4. Si tuviste dudas, m\341rcala como favorita para repasarla.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p6.text(rx + 12, 581, "5. Avanza con el bot\363n [Siguiente] a la pr\363xima palabra.", font="/F1", size=8, color=(0.05, 0.28, 0.63))

    p6.draw_card_box(rx, 435, rw, 125, title="CONTROLES DEL M\323DULO DE REPASO")
    p6.text(rx + 12, 525, "\225 Barra de Progreso Din\341mica:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p6.text(rx + 12, 512, "Indica con precisi\363n cu\341ntas tarjetas has estudiado.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p6.text(rx + 12, 499, "\225 Filtros: 'Todas' vs 'Favoritas':", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p6.text(rx + 12, 486, "Permite estudiar todo el mazo o aislar s\363lo las dif\355ciles.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p6.text(rx + 12, 473, "\225 Bot\363n de Pronunciaci\363n en Tarjeta:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p6.text(rx + 12, 460, "Escucha la definici\363n mientras observas el texto.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p6.text(rx + 12, 447, "\225 Navegaci\363n Bidireccional:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p6.text(rx + 12, 435, "Vuelve atr\341s en cualquier momento con [Anterior].", font="/F2", size=8, color=(0.3, 0.35, 0.4))

    p6.draw_card_box(rx, 315, rw, 110, title="CONSEJOS PARA EL ESTUDIANTE")
    p6.text(rx + 12, 390, "\225 Sesi\363n Recomendada:", font="/F1", size=8.5, color=(0.1, 0.55, 0.2))
    p6.text(rx + 12, 377, "Dedica 15 a 20 minutos diarios antes de cada clase.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p6.text(rx + 12, 364, "\225 Repaso Pre-Examen:", font="/F1", size=8.5, color=(0.1, 0.55, 0.2))
    p6.text(rx + 12, 351, "Filtra por 'Favoritas' y repasa solo tus palabras d\351biles.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p6.text(rx + 12, 338, "\225 Pr\341ctica con compa\361eros:", font="/F1", size=8.5, color=(0.1, 0.55, 0.2))
    p6.text(rx + 12, 325, "Uno lee el concepto y el otro responde la definici\363n.", font="/F2", size=8, color=(0.25, 0.3, 0.35))

    # Bottom guidance table
    y = 280
    p6.draw_section_title(40, y, "10", "DIFERENCIA ENTRE LAS CARAS DE LA FLASHCARD")

    p6.draw_card_box(45, y - 105, 245, 80, title="CARA FRONTAL (PREGUNTA)")
    p6.text(55, y - 55, "\225 T\351rmino econ\363mico destacado en negrita.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p6.text(55, y - 70, "\225 Categor\355a alfab\351tica (Letra A a la U).", font="/F2", size=7.5, color=(0.3, 0.35, 0.4))
    p6.text(55, y - 85, "\225 Indicador visual: 'Toca para voltear'.", font="/F3", size=7.5, color=(0.85, 0.45, 0.05))

    p6.draw_card_box(305, y - 105, 245, 80, title="CARA POSTERIOR (RESPUESTA)")
    p6.text(315, y - 55, "\225 Definici\363n completa y formal de la gu\355a.", font="/F1", size=8, color=(0.1, 0.55, 0.2))
    p6.text(315, y - 70, "\225 Ejemplo pr\341ctico de aplicaci\363n en el mercado.", font="/F2", size=7.5, color=(0.3, 0.35, 0.4))
    p6.text(315, y - 85, "\225 Bot\363n para escuchar la definici\363n por voz.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))


    # ==========================================
    # PÁGINA 7: FAVORITOS & AGREGAR NUEVAS PALABRAS
    # ==========================================
    p7 = pdf.add_page()
    p7.draw_header("MicroDiccionario • Favoritos & Agregar T\351rminos")
    p7.draw_footer(7)

    y = p7.draw_section_title(40, 770, "11", "GESTI\323N DE FAVORITOS Y REGISTRO DE NUEVAS PALABRAS")

    # Phone mockup of Add word dialog
    mockup_add = [
        {"type": "banner", "title": "Agregar T\351rmino Nuevo", "sub": "Base de Datos Local", "h": 26},
        {"type": "card", "title": "T\351rmino:", "desc": "Ej. 'Oligopolio colusivo'", "h": 32},
        {"type": "card", "title": "Significado:", "desc": "Acuerdo formal entre empresas para fijar precios.", "h": 40},
        {"type": "card", "title": "Ejemplo de mercado:", "desc": "Cartel de distribuidores de combustible.", "h": 40},
        {"type": "banner", "title": "[Cancelar]      [Guardar Palabra]", "sub": "Almacenamiento persistente Room", "h": 26},
    ]
    p7.draw_phone_mockup(45, 330, 210, 390, "Formulario Agregar Palabra", mockup_add)

    # Explanation cards right side
    rx = 275
    rw = 280

    p7.draw_card_box(rx, 570, rw, 150, title="C\323MO AGREGAR T\311RMINOS PERSONALIZADOS")
    p7.text(rx + 12, 685, "\277Viste un t\351rmino nuevo en clase?", font="/F1", size=8.5, color=(0.85, 0.45, 0.05))
    p7.text(rx + 12, 672, "Puedes a\361adir tus propios conceptos al diccionario local:", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p7.text(rx + 12, 656, "1. En la pantalla Inicio, toca el bot\363n flotante de tres puntos.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p7.text(rx + 12, 643, "2. Selecciona la opci\363n 'Agregar Palabra'.", font="/F1", size=8, color=(0.05, 0.28, 0.63))
    p7.text(rx + 12, 630, "3. Completa los campos: T\351rmino, Significado y Ejemplo.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p7.text(rx + 12, 617, "4. Presiona 'Guardar'.", font="/F1", size=8, color=(0.1, 0.55, 0.2))
    p7.text(rx + 12, 604, "5. La palabra se almacena inmediatamente en la base de datos", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p7.text(rx + 12, 591, "Room SQLite y se integra a la b\372squeda y flashcards.", font="/F2", size=8, color=(0.2, 0.25, 0.3))
    p7.text(rx + 12, 578, "Se conserva incluso si cierras la aplicaci\363n.", font="/F3", size=7.5, color=(0.05, 0.28, 0.63))

    p7.draw_card_box(rx, 435, rw, 125, title="GESTI\323N Y REPASO DE FAVORITOS")
    p7.text(rx + 12, 525, "\225 Guardar en Favoritos:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p7.text(rx + 12, 512, "Toca el icono de marcador en cualquier tarjeta o ficha.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p7.text(rx + 12, 499, "\225 Ver lista de Favoritos:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p7.text(rx + 12, 486, "Usa el men\372 flotante de tres puntos > 'Ver Favoritos'.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p7.text(rx + 12, 473, "\225 Eliminar de Favoritos:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p7.text(rx + 12, 460, "Vuelve a tocar el marcador para desmarcarlo.", font="/F2", size=8, color=(0.3, 0.35, 0.4))
    p7.text(rx + 12, 447, "\225 Integraci\363n Flashcard:", font="/F1", size=8.5, color=(0.1, 0.15, 0.2))
    p7.text(rx + 12, 435, "En Repaso, pulsa 'Favoritas' para estudiar solo este grupo.", font="/F2", size=8, color=(0.3, 0.35, 0.4))

    p7.draw_card_box(rx, 315, rw, 110, title="INTEGRIDAD Y SEGURIDAD DE DATOS")
    p7.text(rx + 12, 390, "\225 Almacenamiento Local:", font="/F1", size=8.5, color=(0.1, 0.55, 0.2))
    p7.text(rx + 12, 377, "Room Database SQLite en la memoria interna segura de Android.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p7.text(rx + 12, 364, "\225 Privacidad Total:", font="/F1", size=8.5, color=(0.1, 0.55, 0.2))
    p7.text(rx + 12, 351, "Tus notas y palabras no salen de tu dispositivo m\363vil.", font="/F2", size=8, color=(0.25, 0.3, 0.35))
    p7.text(rx + 12, 338, "\225 Persistencia Offline:", font="/F1", size=8.5, color=(0.1, 0.55, 0.2))
    p7.text(rx + 12, 325, "100% operativo sin cobertura celular o en modo avi\363n.", font="/F2", size=8, color=(0.25, 0.3, 0.35))

    # Bottom guidance table
    y = 280
    p7.draw_section_title(40, y, "12", "RECOMENDACIONES PARA CREAR T\311RMINOS DE CALIDAD")

    tips = [
        ("Claridad Conceptual:", "Usa oraciones breves y directas que definan el n\372cleo de la idea."),
        ("Ejemplo del Mundo Real:", "A\361ade una situaci\363n concreta de compra, venta, precios o mercado."),
        ("Ortograf\355a y Acentos:", "Coloca tildes para que el motor TTS lo pronuncie con precisi\363n."),
        ("Evita Redundancias:", "No repitas el t\351rmino dentro de su propia definici\363n b\341sica.")
    ]

    ty = y - 35
    for t_name, t_def in tips:
        p7.rounded_rect(45, ty - 1, 6, 6, r=3, fill=(0.85, 0.45, 0.05))
        p7.text(56, ty, t_name, font="/F1", size=8.5, color=(0.85, 0.45, 0.05))
        p7.text(210, ty, t_def, font="/F2", size=8, color=(0.25, 0.3, 0.35))
        ty -= 18


    # ==========================================
    # PÁGINA 8: FAQ & CRÉDITOS INSTITUCIONALES
    # ==========================================
    p8 = pdf.add_page()
    p8.draw_header("MicroDiccionario • FAQ & Cr\351ditos")
    p8.draw_footer(8)

    y = p8.draw_section_title(40, 770, "13", "PREGUNTAS FRECUENTES (FAQ) Y SOLUCI\323N DE PROBLEMAS")

    faqs = [
        ("\277Por qu\351 se cerraba el teclado al escribir?",
         "Problema corregido en v1.0. La barra de b\372squeda ahora es persistente y mantiene el foco sin interrupci\363n mientras escribes cualquier letra."),
        ("\277Qu\351 hago si el micr\363fono de voz no reconoce mis palabras?",
         "Verifica que la app tenga concedido el permiso de micr\363fono en Ajustes > Aplicaciones > MicroDiccionario > Permisos > Micr\363fono: Permitir."),
        ("\277La aplicaci\363n consume datos m\363viles de internet?",
         "No. El glosario de 250 palabras, la base de datos Room, las tarjetas de repaso y el sintetizador TTS funcionan de manera local e independiente."),
        ("\277C\363mo puedo restaurar el glosario si borro alg\372n t\351rmino?",
         "Al reinstalar o actualizar la app, la base de datos ejecuta autom\341ticamente su callback de siembra que recarga los 250 t\351rminos oficiales."),
        ("\277C\363mo cambio la velocidad de la voz del audio?",
         "El motor TTS se ajusta autom\341ticamente a 0.92x para claridad educativa. Puedes ajustar el tono y velocidad global en Ajustes de Android > Accesibilidad > S\355ntesis de voz.")
    ]

    for q, a in faqs:
        p8.rounded_rect(40, y - 48, 515, 48, r=6, fill=(0.96, 0.98, 1.0), stroke=(0.82, 0.88, 0.95), line_width=0.8)
        p8.text(52, y - 14, q, font="/F1", size=8.5, color=(0.05, 0.28, 0.63))
        p8.text(52, y - 27, a[:92], font="/F2", size=8, color=(0.25, 0.3, 0.35))
        if len(a) > 92:
            p8.text(52, y - 39, a[92:], font="/F2", size=8, color=(0.25, 0.3, 0.35))
        y -= 56

    y = p8.draw_section_title(40, y - 10, "14", "FICHA T\311CNICA DEL SOFTWARE Y CR\311DITOS INSTITUCIONALES")

    p8.draw_card_box(40, y - 150, 515, 150, title="ESPECIFICACIONES T\311CNICAS Y DERECHOS ACAD\311MICOS")
    
    specs = [
        ("Nombre Oficial:", "MicroDiccionario de Microeconom\355a (Gu\355a Did\341ctica UMG)"),
        ("Plataforma & Lenguaje:", "Android Nativo \225 100% Kotlin 2.2 \225 Jetpack Compose M3"),
        ("Capa de Persistencia:", "Room Database 2.7.0 (SQLite con migraci\363n destructiva y carga de 250 t\351rminos)"),
        ("Arquitectura:", "MVVM (Model-View-ViewModel) con StateFlow y Corrutinas reactivas"),
        ("Audio & Fon\351tica:", "Android Speech-to-Text API & Text-to-Speech Engine con acento latino"),
        ("Instituci\363n Acad\351mica:", "Universidad Mariano G\341lvez de Guatemala \225 Sede Chimaltenango"),
        ("Facultad & Carrera:", "Facultad de Ingenier\355a en Sistemas de Informaci\363n y Ciencias de la Computaci\363n"),
        ("Curso & Semestre:", "Microeconom\355a (C\363digo 016) \225 Cuarto Semestre \225 Ciclo Lectivo 2026")
    ]

    sy = y - 36
    for sp_title, sp_val in specs:
        p8.text(55, sy, sp_title, font="/F1", size=8, color=(0.05, 0.28, 0.63))
        p8.text(180, sy, sp_val, font="/F2", size=8, color=(0.25, 0.3, 0.35))
        sy -= 14

    # Sign-off box
    p8.rect(40, 50, 515, 30, fill=(0.05, 0.22, 0.52))
    p8.center_text(68, "\251 2026 Universidad Mariano G\341lvez de Guatemala \225 Uso Did\341ctico Exclusivo", font="/F1", size=9, color=(1, 1, 1))
    p8.center_text(56, "Desarrollado con dedicaci\363n por estudiantes de Ingenier\355a en Sistemas.", font="/F3", size=8, color=(0.85, 0.92, 1))

    pdf.build()

if __name__ == "__main__":
    generate()
