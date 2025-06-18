package minesweeper;

import minesweeper.controller.GameController;
import minesweeper.model.Grid;
import minesweeper.service.GameService;
import minesweeper.service.GridService;
import minesweeper.service.MineService;
import minesweeper.ui.ConsoleUserInterface;
import minesweeper.util.InputValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-End Test for Minesweeper Game
 * 1. Complete Flow - Player Wins
 * 2. Complete Flow - Player Loses (Detonate Mine)
 * 3. Multiple Games - Player Plays Again
 */

class MinesweeperE2ETest {
    private ByteArrayOutputStream outputStream; // Captures console output for verification
    private PrintStream originalOutput; // Store a reference to original console output, and restore it after test

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOutput = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOutput);
        System.setIn(System.in);
    }

    // Helper Function to Discover Mine Positions
    private List<String> discoverMinePositions(long seed, int gridSize, int totalMines) {
        MineService mineService = new MineService(seed);
        Grid grid = new Grid(gridSize, totalMines);
        mineService.placeMines(grid, totalMines);

        List<String> minePositions = new ArrayList<>();
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                if (grid.getCell(row, column).isMine()) {
                    String position = (char) ('A' + row) + String.valueOf(column + 1);
                    minePositions.add(position);
                }
            }
        }

        return minePositions;
    }

    // Helper Method to Generate All Safe Moves
    private List<String> generateSafeMoves(int gridSize, List<String> minePositions) {
        List<String> safeMoves = new ArrayList<>();
        for (int row = 0; row < gridSize; row++) {
            for (int column = 0; column < gridSize; column++) {
                String position = (char) ('A' + row) + String.valueOf(column + 1);
                if (!minePositions.contains(position)) {
                    safeMoves.add(position);
                }
            }
        }

        return safeMoves;
    }

    @Test
    @DisplayName("Complete Game Flow - Player Wins")
    void testCompleteGameFlow_PlayerWins() {
        long testSeed = 12345L;
        int gridSize = 3;
        int totalMines = 1;

        List<String> minePositions = discoverMinePositions(testSeed, gridSize, totalMines);
        List<String> safeMoves = generateSafeMoves(gridSize, minePositions);

        // Build Input Simulation
        List<String> inputLines = new ArrayList<>();
        inputLines.add(String.valueOf(gridSize)); // Grid Size Input
        inputLines.add(String.valueOf(totalMines)); // Total Mines Input
        inputLines.addAll(safeMoves); // All Safe Moves to Win

        String input = String.join("\n", inputLines);
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MineService mineService = new MineService(testSeed);
        GridService gridService = new GridService();
        GameService gameService = new GameService(gridService, mineService);
        InputValidator inputValidator = new InputValidator();
        ConsoleUserInterface userInterface = new ConsoleUserInterface(inputValidator) {
            @Override
            public boolean renderPlayAgain() {
                return false;
            }
        };
        GameController gameController = new GameController(userInterface, gameService);

        gameController.startGame();
        String output = outputStream.toString();

        // Verify Game Flow
        assertTrue(output.contains("Welcome to Minesweeper!\n"), "Should display welcomem message.");
        assertTrue(output.contains("Enter the size of the grid (e.g. 4 for a 4x4 grid):"), "Should prompt for grid size.");
        assertTrue(output.contains("Enter the number of mines to place on the grid (maximum is 35% of the total squares):"), "Should prompt for total mines.");
        assertTrue(output.contains("Here is your minefield:"), "Should display game grid on first render.");
        assertTrue(output.contains("Select a square to reveal (e.g. A1):"), "Should prompt for select cell.");
        assertTrue(output.contains("Here is your updated minefield:"), "Should display game grid after select cell.");
        assertTrue(output.contains("Congratulations, you have won the game!"), "Should display game won message.");
        assertFalse(output.contains("Oh no, you detonated a mine! Game over."), "Should not display game lost message.");
    }

    @Test
    @DisplayName("Complete Flow - Player Loses (Detonate Mine)")
    void testCompleteGameFlow_PlayerLoses() {
        long testSeed = 12345L;
        int gridSize = 3;
        int totalMines = 1;

        List<String> minePositions = discoverMinePositions(testSeed, gridSize, totalMines);

        // Build Input Simulation to Hit First Mine
        String input = String.join("\n",
                String.valueOf(gridSize),
                String.valueOf(totalMines),
                minePositions.get(0)
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MineService mineService = new MineService(testSeed);
        GridService gridService = new GridService();
        GameService gameService = new GameService(gridService, mineService);
        InputValidator inputValidator = new InputValidator();
        ConsoleUserInterface userInterface = new ConsoleUserInterface(inputValidator) {
            @Override
            public boolean renderPlayAgain() {
                return false;
            }
        };
        GameController gameController = new GameController(userInterface, gameService);

        gameController.startGame();

        String output = outputStream.toString();

        // Verify Game Flow
        assertTrue(output.contains("Welcome to Minesweeper!\n"), "Should display welcomem message.");
        assertTrue(output.contains("Enter the size of the grid (e.g. 4 for a 4x4 grid):"), "Should prompt for grid size.");
        assertTrue(output.contains("Enter the number of mines to place on the grid (maximum is 35% of the total squares):"), "Should prompt for total mines.");
        assertTrue(output.contains("Here is your minefield:"), "Should display game grid on first render.");
        assertTrue(output.contains("Select a square to reveal (e.g. A1):"), "Should prompt for select cell.");
        assertTrue(output.contains("Oh no, you detonated a mine! Game over."), "Should display game lost message.");
        assertFalse(output.contains("Congratulations, you have won the game!"), "Should not display game won message.");
    }

    @Test
    @DisplayName("Multiple Games - Player Plays Again")
    void testMultipleGames_PlayerPlaysAgain() {
        long testSeed = 12345L;
        int gridSize = 3;
        int totalMines = 1;

        List<String> minePositions = discoverMinePositions(testSeed, gridSize, totalMines);

        String input = String.join("\n",
                String.valueOf(gridSize),  // 1st game grid size
                String.valueOf(totalMines),      // 1st game mine count
                minePositions.get(0),           // Hit mine in 1st game
                "x"                           // Random key
        );
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MineService mineService = new MineService(testSeed);
        GridService gridService = new GridService();
        GameService gameService = new GameService(gridService, mineService);
        InputValidator inputValidator = new InputValidator();
        ConsoleUserInterface userInterface = new ConsoleUserInterface(inputValidator) {
            private int playAgainCount = 0;
            private int gameStartCount = 0;

            @Override
            public boolean renderPlayAgain() {
                playAgainCount++;
                super.renderPlayAgain();
                return playAgainCount == 1; // Play again once, then stop
            }

            @Override
            public void renderWelcomeMessage() {
                gameStartCount++;
                super.renderWelcomeMessage();

                if (gameStartCount >= 2) {
                    throw new RuntimeException("Force Quit");
                }
            }
        };

        GameController gameController = new GameController(userInterface, gameService);

        // Expect the custom exception when we force quit.
        assertThrows(RuntimeException.class, () -> {
            gameController.startGame();
        });
        
        String output = outputStream.toString();

        // Verify Multiple Games
        assertTrue(output.contains("Welcome to Minesweeper!"), "Should display welcome message");
        assertTrue(output.contains("Press any key to play again"), "Should prompt to play again");

        // Count the occurence of welcome message. (if >1, meaning it played multiple times).
        int welcomeCount = output.split("Welcome to Minesweeper!", -1).length - 1;
        assertTrue(welcomeCount >= 2, "Should have multiple welcome messages for multiple games");
    }
}
