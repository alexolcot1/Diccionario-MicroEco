import os
import zlib
import struct

def escape_str(text):
    out = ""
    for c in text:
        b = c.encode("latin-1", errors="replace")[0]
        if chr(b) in ("(", ")", "\\"):
            out += "\\" + chr(b)
        elif 32 <= b <= 126:
            out += chr(b)
        else:
            out += f"\\{b:03o}"
    return out

class PDFBuilder:
    def __init__(self, filename="manual_de_usuario.pdf"):
        self.filename = filename
        self.pages = []
        self.image_bytes = None
        self.image_w = 0
        self.image_h = 0
        self.width = 595.28
        self.height = 841.89

    def load_image(self, path):
        if os.path.exists(path):
            with open(path, "rb") as f:
                self.image_bytes = f.read()
            # Get JPEG dimensions
            # Scan SOF0/SOF2 marker
            data = self.image_bytes
            i = 0
            while i < len(data) - 8:
                if data[i] == 0xFF and data[i+1] in (0xC0, 0xC2):
                    h, w = struct.unpack(">HH", data[i+5:i+9])
                    self.image_w = w
                    self.image_h = h
                    break
                i += 1

    def add_page(self):
        page = PageBuilder(self.width, self.height)
        self.pages.append(page)
        return page

    def build(self):
        objects = []
        
        # 1: Catalog
        objects.append("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj")
        
        # 2: Pages
        kids_refs = []
        current_obj = 3
        # We will assign obj IDs
        # Page objects start from current_obj
        page_objs_start = 3
        num_pages = len(self.pages)
        
        # font objs
        font_base = 3 + num_pages * 2  # page obj + content obj for each
        f_bold = font_base
        f_regular = font_base + 1
        f_italic = font_base + 2
        f_mono = font_base + 3
        
        img_obj_id = f_mono + 1 if self.image_bytes else None
        
        page_obj_ids = []
        content_obj_ids = []
        for p_idx in range(num_pages):
            p_id = 3 + p_idx * 2
            c_id = p_id + 1
            page_obj_ids.append(p_id)
            content_obj_ids.append(c_id)
            kids_refs.append(f"{p_id} 0 R")
            
        objects.append(f"2 0 obj\n<< /Type /Pages /Kids [{' '.join(kids_refs)}] /Count {num_pages} >>\nendobj")
        
        # Add pages and contents
        for p_idx, page in enumerate(self.pages):
            p_id = page_obj_ids[p_idx]
            c_id = content_obj_ids[p_idx]
            
            xobj_entry = f"/Im1 {img_obj_id} 0 R" if img_obj_id else ""
            res = f"<< /Font << /F1 {f_bold} 0 R /F2 {f_regular} 0 R /F3 {f_italic} 0 R /F4 {f_mono} 0 R >> /XObject << {xobj_entry} >> >>"
            objects.append(f"{p_id} 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 {self.width:.2f} {self.height:.2f}] /Contents {c_id} 0 R /Resources {res} >>\nendobj")
            
            raw_content = page.get_stream()
            compressed = zlib.compress(raw_content.encode("latin-1"))
            stream_obj = f"{c_id} 0 obj\n<< /Length {len(compressed)} /Filter /FlateDecode >>\nstream\n".encode("latin-1") + compressed + b"\nendstream\nendobj"
            objects.append(stream_obj)
            
        # Fonts
        objects.append(f"{f_bold} 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold /Encoding /WinAnsiEncoding >>\nendobj")
        objects.append(f"{f_regular} 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>\nendobj")
        objects.append(f"{f_italic} 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Oblique /Encoding /WinAnsiEncoding >>\nendobj")
        objects.append(f"{f_mono} 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Courier /Encoding /WinAnsiEncoding >>\nendobj")
        
        if self.image_bytes and img_obj_id:
            img_hdr = f"{img_obj_id} 0 obj\n<< /Type /XObject /Subtype /Image /Width {self.image_w} /Height {self.image_h} /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length {len(self.image_bytes)} >>\nstream\n".encode("latin-1")
            img_full = img_hdr + self.image_bytes + b"\nendstream\nendobj"
            objects.append(img_full)
            
        # Build final PDF binary
        pdf = b"%PDF-1.4\n"
        offsets = []
        for obj in objects:
            offsets.append(len(pdf))
            if isinstance(obj, str):
                pdf += obj.encode("latin-1") + b"\n"
            else:
                pdf += obj + b"\n"
                
        xref_pos = len(pdf)
        pdf += f"xref\n0 {len(objects) + 1}\n0000000000 65535 f \n".encode("latin-1")
        for off in offsets:
            pdf += f"{off:010d} 00000 n \n".encode("latin-1")
            
        pdf += f"trailer\n<< /Size {len(objects) + 1} /Root 1 0 R >>\nstartxref\n{xref_pos}\n%%EOF".encode("latin-1")
        
        with open(self.filename, "wb") as f:
            f.write(pdf)
        print(f"Wrote {self.filename} ({len(pdf)} bytes, {num_pages} pages)")


class PageBuilder:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.cmds = []

    def rect(self, x, y, w, h, fill=None, stroke=None, line_width=1):
        if fill:
            r, g, b = fill
            self.cmds.append(f"{r:.3f} {g:.3f} {b:.3f} rg")
        if stroke:
            r, g, b = stroke
            self.cmds.append(f"{r:.3f} {g:.3f} {b:.3f} RG")
            self.cmds.append(f"{line_width:.2f} w")
        
        self.cmds.append(f"{x:.2f} {y:.2f} {w:.2f} {h:.2f} re")
        if fill and stroke:
            self.cmds.append("B")
        elif fill:
            self.cmds.append("f")
        elif stroke:
            self.cmds.append("S")

    def rounded_rect(self, x, y, w, h, r=6, fill=None, stroke=None, line_width=1):
        # Approximate rounded rectangle with bezier
        c = 0.552284749831 * r
        d = []
        if fill:
            rf, gf, bf = fill
            d.append(f"{rf:.3f} {gf:.3f} {bf:.3f} rg")
        if stroke:
            rs, gs, bs = stroke
            d.append(f"{rs:.3f} {gs:.3f} {bs:.3f} RG")
            d.append(f"{line_width:.2f} w")
            
        d.append(f"{x + r:.2f} {y:.2f} m")
        d.append(f"{x + w - r:.2f} {y:.2f} l")
        d.append(f"{x + w - r + c:.2f} {y:.2f} {x + w:.2f} {y + r - c:.2f} {x + w:.2f} {y + r:.2f} c")
        d.append(f"{x + w:.2f} {y + h - r:.2f} l")
        d.append(f"{x + w:.2f} {y + h - r + c:.2f} {x + w - r + c:.2f} {y + h:.2f} {x + w - r:.2f} {y + h:.2f} c")
        d.append(f"{x + r:.2f} {y + h:.2f} l")
        d.append(f"{x + r - c:.2f} {y + h:.2f} {x:.2f} {y + h - r + c:.2f} {x:.2f} {y + h - r:.2f} c")
        d.append(f"{x:.2f} {y + r:.2f} l")
        d.append(f"{x:.2f} {y + r - c:.2f} {x + r - c:.2f} {y:.2f} {x + r:.2f} {y:.2f} c")
        d.append("h")
        
        if fill and stroke:
            d.append("B")
        elif fill:
            d.append("f")
        elif stroke:
            d.append("S")
        self.cmds.append(" ".join(d))

    def line(self, x1, y1, x2, y2, stroke=(0.8, 0.8, 0.8), line_width=1):
        r, g, b = stroke
        self.cmds.append(f"{r:.3f} {g:.3f} {b:.3f} RG {line_width:.2f} w {x1:.2f} {y1:.2f} m {x2:.2f} {y2:.2f} l S")

    def text(self, x, y, text, font="/F2", size=10, color=(0.1, 0.1, 0.1)):
        r, g, b = color
        esc = escape_str(text)
        self.cmds.append(f"BT {font} {size:.1f} Tf {r:.3f} {g:.3f} {b:.3f} rg {x:.2f} {y:.2f} Td ({esc}) Tj ET")

    def center_text(self, y, text, font="/F1", size=14, color=(0.1, 0.1, 0.1)):
        # Approximate width based on font
        char_w = size * (0.6 if "Bold" in font else 0.52)
        total_w = len(text) * char_w
        x = max(20, (self.width - total_w) / 2)
        self.text(x, y, text, font=font, size=size, color=color)

    def draw_image(self, x, y, w, h):
        self.cmds.append(f"q {w:.2f} 0 0 {h:.2f} {x:.2f} {y:.2f} cm /Im1 Do Q")

    def draw_header(self, title, subtitle="Universidad Mariano Gálvez de Guatemala"):
        # Top banner bar
        self.rect(0, self.height - 40, self.width, 40, fill=(0.05, 0.28, 0.63))
        self.text(30, self.height - 24, subtitle, font="/F1", size=10, color=(1, 1, 1))
        self.text(self.width - 200, self.height - 24, "MicroDiccionario • Manual", font="/F2", size=9, color=(0.85, 0.92, 1))
        self.line(0, self.height - 41, self.width, self.height - 41, stroke=(0.8, 0.85, 0.9), line_width=1)

    def draw_footer(self, page_num, total_pages=8):
        y = 30
        self.line(30, y + 15, self.width - 30, y + 15, stroke=(0.82, 0.86, 0.9), line_width=0.8)
        self.text(30, y, "Manual de Usuario Oficial • MicroDiccionario v1.0 • UMG 2026", font="/F3", size=8, color=(0.4, 0.45, 0.5))
        self.text(self.width - 90, y, f"Página {page_num} de {total_pages}", font="/F1", size=8.5, color=(0.05, 0.28, 0.63))

    def draw_section_title(self, x, y, number, title):
        # Number badge
        self.rounded_rect(x, y - 4, 28, 22, r=4, fill=(0.05, 0.28, 0.63))
        self.text(x + 7, y + 2, number, font="/F1", size=12, color=(1, 1, 1))
        self.text(x + 36, y + 2, title, font="/F1", size=15, color=(0.05, 0.28, 0.63))
        self.line(x, y - 8, self.width - 40, y - 8, stroke=(0.85, 0.88, 0.92), line_width=1)
        return y - 22

    def draw_card_box(self, x, y, w, h, title="", fill=(0.96, 0.97, 0.99), border=(0.8, 0.85, 0.92)):
        self.rounded_rect(x, y, w, h, r=8, fill=fill, stroke=border, line_width=1)
        if title:
            self.rounded_rect(x, y + h - 22, w, 22, r=8, fill=(0.91, 0.94, 0.98))
            self.text(x + 12, y + h - 15, title, font="/F1", size=10, color=(0.05, 0.28, 0.63))

    def draw_phone_mockup(self, x, y, w, h, screen_title, content_items):
        # Outer phone body
        self.rounded_rect(x, y, w, h, r=16, fill=(0.98, 0.98, 0.99), stroke=(0.2, 0.25, 0.32), line_width=2.5)
        # Notch / Camera pill
        self.rounded_rect(x + w/2 - 20, y + h - 14, 40, 6, r=3, fill=(0.2, 0.25, 0.32))
        # Screen header
        self.rounded_rect(x + 4, y + h - 42, w - 8, 26, r=4, fill=(0.05, 0.28, 0.63))
        self.text(x + 14, y + h - 33, screen_title, font="/F1", size=9, color=(1, 1, 1))
        
        # Screen content
        cy = y + h - 60
        for item in content_items:
            itype = item.get("type", "text")
            if itype == "search":
                # Draw search bar
                self.rounded_rect(x + 10, cy - 14, w - 20, 20, r=10, fill=(1, 1, 1), stroke=(0.75, 0.8, 0.88), line_width=1)
                self.text(x + 18, cy - 9, item.get("text", "Buscar..."), font="/F3", size=7.5, color=(0.4, 0.45, 0.5))
                # Mic button
                self.rounded_rect(x + w - 28, cy - 12, 16, 16, r=8, fill=(0.9, 0.94, 1))
                self.text(x + w - 24, cy - 8, "[Voz]", font="/F1", size=5.5, color=(0.05, 0.28, 0.63))
                cy -= 26
            elif itype == "card":
                ch = item.get("h", 38)
                self.rounded_rect(x + 10, cy - ch, w - 20, ch, r=6, fill=(1, 1, 1), stroke=(0.85, 0.88, 0.92), line_width=0.8)
                self.text(x + 16, cy - 12, item.get("title", ""), font="/F1", size=8, color=(0.05, 0.28, 0.63))
                desc = item.get("desc", "")
                self.text(x + 16, cy - 22, desc[:36], font="/F2", size=6.5, color=(0.3, 0.3, 0.3))
                if len(desc) > 36:
                    self.text(x + 16, cy - 30, desc[36:72], font="/F2", size=6.5, color=(0.3, 0.3, 0.3))
                cy -= (ch + 8)
            elif itype == "banner":
                bh = item.get("h", 30)
                self.rounded_rect(x + 10, cy - bh, w - 20, bh, r=6, fill=(0.92, 0.95, 0.99), stroke=(0.78, 0.85, 0.95), line_width=0.8)
                self.text(x + 16, cy - 12, item.get("title", ""), font="/F1", size=8, color=(0.05, 0.28, 0.63))
                self.text(x + 16, cy - 22, item.get("sub", ""), font="/F2", size=6.5, color=(0.35, 0.4, 0.45))
                cy -= (bh + 8)
            elif itype == "spacer":
                cy -= item.get("h", 10)

        # Bottom nav bar
        self.rounded_rect(x + 4, y + 4, w - 8, 24, r=4, fill=(0.95, 0.96, 0.98), stroke=(0.85, 0.88, 0.92), line_width=0.6)
        tabs = ["Inicio", "Explorar", "Repaso", "Acerca"]
        tw = (w - 8) / 4
        for ti, tab in enumerate(tabs):
            tx = x + 4 + ti * tw
            is_active = (tab == item.get("active_tab", "Inicio"))
            tcolor = (0.05, 0.28, 0.63) if is_active else (0.5, 0.55, 0.6)
            tfont = "/F1" if is_active else "/F2"
            self.text(tx + tw/2 - 12, y + 11, tab, font=tfont, size=6.5, color=tcolor)

    def get_stream(self):
        return "\n".join(self.cmds)

print("PDF builder module ready!")
