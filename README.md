# Blackjack Game with Power-Ups

This is a single-player Blackjack game written in Java, featuring unique gameplay mechanics such as custom power-ups, a persistent vault system, and a clean, modular codebase ready for future GUI integration.

## Features

- **Standard Blackjack gameplay** — hit, stand, bust (no push/tie resolution beyond declared tie).
- **Custom Power-Up system** — unique effects like All or Nothing, Pick Two, Double Jeopardy.
- **Vault system** — store cards persistently across rounds.
- **Object-oriented design** — clear separation of game-wide state (GameContext), round-specific state (RoundContext), and game flow (GameEnvironment/RoundEnvironment).
- **Refactored architecture** — logic is now decoupled from console input/output to prepare for GUI implementations (e.g., JavaFX).

## Technical Details

- Developed in Java.
- Modular, maintainable, and MVC-ready code structure.
- Uses enums to encapsulate Power-Up behavior, rarity, and cost logic.
- Game state (`GameContext`) and round state (`RoundContext`) are fully separated from input/output layers (`GameEnvironment`, `RoundEnvironment`), enabling easy replacement of the console UI with a GUI.

## How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/jakem1241/blackjack-game.git
   cd blackjack-game
2. Compile and run:
   ```bash
   javac src/Main.java
   java -cp src Main
Or simply run in your IDE of choice.

## Future Plans

- JavaFX GUI — thanks to the refactored architecture, game logic can plug directly into a graphical interface without major rewrites.
- Additional power-ups, card animations, and statistics tracking.

Created by Charles (Jake) Matthews