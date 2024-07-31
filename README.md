# Chip8 Java Emulator

A Chip-8 Interpreter written in Java.

## Building and Running:

To build the project, run the following command in the terminal:

```bash
$ cd path/to/project
$ javac *.java
```

Running from the command line:

```bash
$ java Chip8 path/to/rom
```

Running from the command line with logging enabled:

```bash
$ java Chip8 path/to/rom log
```

### Compatibility:

Compatible with Linux, macOS, and Windows.

### Dependencies:

- JDK (Java Development Kit)
- A copy of a Chip-8 ROM

## Keyboard Layout:

### Chip8 Keypad:
|   |   |   |   |
|---|---|---|---|
| 1 | 2 | 3 | C |
| 4 | 5 | 6 | D |
| 7 | 8 | 9 | E |
| A | 0 | B | F |

### Emulator Keyboard Mapping:
|   |   |   |   |
|---|---|---|---|
| 1 | 2 | 3 | 4 |
| Q | W | E | R |
| A | S | D | F |
| Z | X | C | V |

**Escape Key (`esc`)** : Close the Emulator  
**Spacebar** : Pause / Resume the Emulator  
**F5 Key** : Reset the emulator  
---

Adjust the paths and URLs (`path/to/project`, `path/to/rom`, etc.) to match your project's specifics. This rewritten text mirrors the functionality and setup instructions while adapting it for a Java-based implementation of the Chip-8 Interpreter.