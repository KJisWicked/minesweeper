# Minesweeper

This ia a mini minesweeper program built with Java.

## Spec

- Java/JDK 21
- Maven

## Project Structure

### Main Package

This is an overview of the project structure which adopts an MVC (Model-View-Controller) but using OOP & Solid
Principles.

### Class Responsibilities

1. `MineSweeper`: Application entry point and dependency wiring
2. `GameController`: Orchestrates game flow and user interactions
3. `GameService`: Core game logic and rule enforcement
4. `GridService`: Grid manipulation and flood-fill algorithm
5. `MineService`: Mine placement and validation
6. `ConsoleUserInterface`: Console I/O operations
7. `InputValidator`: Input validation and parsing
8. `Cell/Grid/GameState`: Data models representing game state

```
src/main/java/minesweeper/
├── MineSweeper.java                # Main entry point with DI setup
├── model/                          # Data models
│   ├── Cell.java                   # Individual cell representation
│   ├── Grid.java                   # Game grid container
│   └── GameState.java              # Game state management
├── service/                        # Business logic layer
│   ├── GameService.java            # Main game orchestration
│   ├── GridService.java            # Grid operations & flood-fill
│   └── MineService.java            # Mine placement logic
├── ui/                             # User interface layer
│   ├── UserInterface.java          # UI abstraction interface
│   └── ConsoleUserInterface.java   # Console implementation
├── util/                           # Utility classes
│   └── InputValidator.java         # Input validation logic
└── game/                           # Game control layer
    └── GameController.java         # Main game flow controller
```

###

### Test Package

The test package is also located within the project but in another directory `src/test/`.

For the tests, I have used a combination of both `JUnit` and `Mockito` to write my Unit & End-to-End test cases.

```
src/test/java/minesweeper/
├── GameServiceTest.java
├── GridServiceTest.java
├── InputValidatorTest.java
├── MineServiceTest.java
└── MinesweeperE2ETest.java
```

## Run The Project

There's no specific setup as it is a very basic Java + Maven Project. You can just do the following

1. Clone the project.
2. Open via your IDE either with IntelliJ IDEA or Eclipse.
3. Build the project using the IDE and begin the game.

### Game Instructions

1. Start the game.
2. Enter grid size: Choose a number between 3-20 for grid dimensions
3. Enter mine count: Choose number of mines (max 35% of total squares)
4. Make moves: Enter coordinates like "A1", "B2", etc.
5. Win condition: Reveal all non-mine squares
6. Lose condition: Reveal a mine
7. Whenever you win or lose, you can click any key to play again.

### Sample Gameplay

```
Welcome to Minesweeper!

Enter the size of the grid (e.g. 4 for a 4x4 grid): 
4
Enter the number of mines to place on the grid (maximum is 35% of the total squares): 
3

Here is your minefield:
  1 2 3 4
A _ _ _ _
B _ _ _ _
C _ _ _ _
D _ _ _ _

Select a square to reveal (e.g. A1): A1
This square contains 2 adjacent mines.
```


