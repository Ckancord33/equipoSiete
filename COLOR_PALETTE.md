# DogApp Color Palette & UI Color Guide

This document defines the official color tokens for **DogApp** in [`app/src/main/res/values/colors.xml`](app/src/main/res/values/colors.xml) so every developer and AI agent uses the exact same colors across all User Stories (**HU 1.0 – HU 5.0**).

## Rules for AI Agents

1. **Never hardcode hex colors** in XML layouts, drawables, or Kotlin files. Always reference `@color/<token_name>` from `app/src/main/res/values/colors.xml`.
2. **Reuse existing tokens** before adding new colors. Only add a new color to `colors.xml` and this document if no existing semantic token fits the User Story requirement.
3. **Keep resource names in English** (`snake_case`).

---

## Color Tokens (`colors.xml`)

| Resource Name | Hex (ARGB) | Visual Swatch | Semantic Role & Usage |
|---|---|---|---|
| `white` | `#FFFFFFFF` | White | `HU 2.0` screen background (criterion 2: white background), toolbar/header titles, brand title `"DogApp"`, icon tints inside FABs, enabled primary button text (`"Guardar Cita"`, `"Editar Cita"`). |
| `black` | `#FF000000` | Black | High-contrast titles when required (e.g., breed title in `HU 4.0` detail card). |
| `home_header_background` | `#FF242B30` | Dark Slate Gray | Login screen full background (`HU 1.0`) and curved dark headers/toolbars (`HU 2.0`, `HU 3.0`, `HU 4.0`, `HU 5.0`). |
| `home_background` | `#FFFAF9FB` | Off-White | Main screen background behind cards/forms (`HU 3.0`, `HU 4.0`, `HU 5.0`), allowing white cards to stand out with elevation. |
| `home_card_background` | `#FFFFFFFF` | Pure White | Elevated rounded cards (`CardView`) in `HU 2.0` list items and `HU 4.0` photo/detail cards. |
| `accent_pink` | `#FFF05861` | Coral Pink | Primary accent (`#F05861` from HUs): Home `+` FAB, appointment turn `#ID`, toolbar back arrows, detail divider line, delete FAB, and enabled `"Guardar Cita"` / `"Editar Cita"` buttons. |
| `accent_pink_disabled` | `#FFF5A3A8` | Muted Pink | Disabled state for primary form buttons (`"Guardar Cita"`, `"Editar Cita"`) when required fields are incomplete. |
| `text_primary` | `#FF333333` | Charcoal | Primary body/card titles (e.g., pet name in `HU 2.0`, input text). |
| `text_secondary` | `#FF666666` | Medium Gray | Secondary labels, hints, symptom text, owner name, and phone number. |
| `input_focus_purple` | `#FF6200EE` | Purple | Focused `TextInputLayout` border and floating hint highlight in `HU 3.0` and `HU 5.0`. |
| `input_stroke_default` | `#FFBDBDBD` | Light Gray | Unfocused `TextInputLayout` outline border in `HU 3.0` and `HU 5.0`. |
| `action_edit_dark_blue` | `#FF1B263B` | Dark Navy Blue | Edit floating action button (`HU 4.0` bottom-right). |

---

## Screen-by-Screen Mapping (User Stories)

### HU 1.0 — Ventana Login
- **Screen background:** `@color/home_header_background` (`#242B30`, no toolbar).
- **Title `"DogApp"`:** `@color/white` bold.
- **Top-right dog image:** Transparent background asset.
- **Biometric prompt:** Native system `BiometricPrompt` dialog.

### HU 2.0 — Home Administrador de Citas (Reference Layout)
Implemented in [`fragment_home.xml`](app/src/main/res/layout/fragment_home.xml) and [`item_appointment.xml`](app/src/main/res/layout/item_appointment.xml):
- **Screen background:** `@color/white` (`#FFFFFF`), as required by HU 2.0 criterion 2.
- **Curved header (`bg_home_header.xml`):** `@color/home_header_background` (`#242B30`) with `bottomLeftRadius="72dp"`.
- **Header texts (`"Administrador\nde Citas"` & `"DogApp"`):** `@color/white` bold.
- **Appointment card:** `@color/home_card_background` (`#FFFFFF`) with `cardCornerRadius="12dp"` and `cardElevation="3dp"`.
- **Pet name:** `@color/text_primary` (`#333333`) bold.
- **Symptom description:** `@color/text_secondary` (`#666666`).
- **Turn badge (`# 1`):** `@color/accent_pink` (`#F05861`) bold.
- **Add FAB (`+`):** Background `@color/accent_pink` (`#F05861`), icon tint `@color/white`, rounded square (`16dp` corner size).

### HU 3.0 — Ventana Nueva Cita
- **Screen background:** `@color/home_background` (`#FAF9FB`).
- **Curved toolbar:** `@color/home_header_background` (`#242B30`), title `"Nueva Cita"` in `@color/white` bold, back arrow in `@color/accent_pink` (`#F05861`).
- **Text fields (`Nombre de la Mascota`, `Raza`, `Nombre del Propietario`, `Teléfono`):**
  - Default hint & stroke: `@color/text_secondary` / `@color/input_stroke_default`.
  - Focused border & floating hint: `@color/input_focus_purple` (`#6200EE`).
- **Button `"Guardar Cita"`:**
  - Disabled (missing fields): `@color/accent_pink_disabled` (`#F5A3A8`).
  - Enabled (all required fields filled): `@color/accent_pink` (`#F05861`) with `@color/white` bold text.

### HU 4.0 — Ventana Detalle de la Cita
- **Screen background:** `@color/home_background` (`#FAF9FB`).
- **Curved toolbar:** `@color/home_header_background` (`#242B30`), pet name title in `@color/white` bold, back arrow in `@color/accent_pink` (`#F05861`).
- **Photo card & details card:** `@color/home_card_background` (`#FFFFFF`) with elevation and rounded corners.
- **Turn (`#1`):** `@color/accent_pink` (`#F05861`).
- **Breed title:** `@color/black` (`#000000`) or `@color/text_primary` bold.
- **Divider line:** `@color/accent_pink` (`#F05861`).
- **Symptom, `Propietario`, `Teléfono`:** `@color/text_secondary` (`#666666`).
- **Delete FAB (bottom-left):** `@color/accent_pink` (`#F05861`), icon `@color/white`.
- **Edit FAB (bottom-right):** `@color/action_edit_dark_blue` (`#1B263B`), icon `@color/white`.

### HU 5.0 — Ventana Editar Cita
- Shares the exact same color tokens as **HU 3.0** (`home_header_background` toolbar, `accent_pink` back arrow, `input_focus_purple` focused fields, and `accent_pink` / `accent_pink_disabled` for `"Editar Cita"`).
