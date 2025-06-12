# Blackjack Game with Power-Ups

This is a single-player Blackjack game written in Java, featuring unique gameplay mechanics such as custom power-ups, persistent player state, and a vault system that carries across rounds. Designed for replayability and clear code structure.

## Features

- Standard Blackjack gameplay (hit, stand, bust, NO PUSH)
- Custom Power-Up system with unique effects (e.g., All or Nothing, Pick Two, Double Jeopardy)
- Vault system that persists player progress across rounds
- Object-oriented design with classes like GameContext, RoundContext, Card, and PowerUp
- Console-based UI with clear round flow and player prompts

## Technical Details

- Developed in Java
- Modular and maintainable code structure
- Uses enums for Power-Up behavior and cost logic
- Separation of game state and round state for clarity

## How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/jakem1241/blackjack-game.git
   cd blackjack-game
2. Compile and run:
javac src/Main.java
java -cp src Main
Or run directly in an IDE.

Created by Charles (Jake) Matthews
