# Minesweeper (Java Swing)

A desktop implementation of the classic Minesweeper game built in Java using Swing and AWT. Developed as part of the Multiplatform Application Development (DAM) curriculum, focusing on Object-Oriented Programming (OOP) principles, clean event handling, and recursive grid algorithms.

---

## Features

- **Dynamic Grid Sizing:** Switch between grid dimensions on the fly via the menu bar:
  - Small (8 × 8)
  - Standard (10 × 10)
  - Large (12 × 12)
- **Difficulty Selection:** Change the mine density directly from the control panel (`JComboBox`):
  - Easy (10 mines)
  - Medium (15 mines)
  - Hard (20 mines)
- **Recursive Flood-Fill:** Automatically uncovers connected blank cells (`0`) and their numeric borders upon selection using a recursive traversal algorithm.
- **Full Game State Management:**
  - Real-time win detection when all non-mine cells are cleared.
  - Loss detection with instant board reveal exposing all hidden mines (`💣`).
  - Safe board reset without restarting the runtime application.
- **Multi-Window Navigation:** Integrated secondary `JFrame` window displaying gameplay rules and instructions.

---

## Technical Highlights & Architecture

- **Custom Component Encapsulation (`Boton extends JButton`):** Inherits from `JButton` while encapsulating immutable coordinate properties (`fila`, `columna`), eliminating external coordinate tracking lookups.
- **Centralized Event Listener:** All grid buttons share a single `ActionListener` instance, extracting the origin component via `e.getSource()` to minimize memory footprint in the JVM heap.
- **Robust Swing Layout Management:**
  - Root `JFrame` structured via `BorderLayout` (North, Center, South).
  - Central board strictly managed via `GridBagLayout` and `GridBagConstraints` for responsive cell scaling.
  - Dynamic UI reconstruction using `panelCentral.removeAll()`, `revalidate()`, and `repaint()`.
- **Safe Recursion Bounds Checking:** Zero out-of-bounds risk by placing matrix boundary assertions (`fila < 0 || fila >= filas...`) as early guard clauses before array access.

---

## Project Structure

```text
├── src/
│   ├── Buscaminas.java           # Main game window, UI layout, logic, and entry point
│   ├── Boton.java                # Custom JButton subclass holding cell coordinates
│   └── VentanaInformativa.java   # Secondary JFrame displaying gameplay guidelines
└── README.md
