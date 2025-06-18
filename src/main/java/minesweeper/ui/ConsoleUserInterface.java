package minesweeper.ui;

import minesweeper.model.Cell;
import minesweeper.model.GameState;
import minesweeper.model.Grid;
import minesweeper.util.InputValidator;

import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {

    private final Scanner scanner;
    private final InputValidator inputValidator;

    public ConsoleUserInterface(InputValidator inputValidator) {
        this.scanner = new Scanner(System.in);
        this.inputValidator = inputValidator;
    }

    @Override
    public void renderWelcomeMessage() {
        renderMessage("Welcome to Minesweeper!\n");
    }

    @Override
    public int renderGetGridSize() {
        while (true) {
            renderMessage("Enter the size of the grid (e.g. 4 for a 4x4 grid): ");
            String input = scanner.nextLine().trim();

            if (inputValidator.isValidGridSize(input)) {
                return Integer.parseInt(input);
            }

            renderMessage("Please enter a valid grid size (min 3, max 20)");
        }
    }

    @Override
    public int renderGetTotalMines(int gridSize, int maxMines) {
        while (true) {
            renderMessage("Enter the number of mines to place on the grid (maximum is 35% of the total squares):");
            String input = scanner.nextLine().trim();

            if (inputValidator.isValidMineCount(input, gridSize, maxMines)) {
                return Integer.parseInt(input);
            }

            renderMessage(String.format("Please enter allowed number of mines (1 to %d).", maxMines));
        }
    }

    @Override
    public int[] renderGetCellSelection(int gridSize) {
        while (true) {
            renderMessage("Select a square to reveal (e.g. A1): ");
            String input = scanner.nextLine().trim().toUpperCase();

            int[] coordinates = inputValidator.parseCoordinates(input, gridSize);
            if (coordinates != null) {
                return coordinates;
            }

            renderMessage("Please enter a valid coordinate (e.g. A1, B2, etc.).");
        }
    }

    @Override
    public void renderGrid(GameState gameState) {
        Grid grid = gameState.getGrid();
        int size = grid.getSize();

        renderMessage("");
        renderMessage("Here is your " + (gameState.getRevealedCells() == 0 ? "" : "updated ") + "minefield:");

        // Render Header Row
        System.out.print(" " + " ");
        for (int column = 1; column <= size; column++) {
            System.out.print(column + " ");
        }
        System.out.println();

        // Render Subsequent Rows
        for (int row = 0; row < size; row++) {
            char rowLabel = (char) ('A' + row);
            System.out.print(rowLabel + " ");

            for (int column = 0; column < size; column++) {
                Cell cell = grid.getCell(row, column);
                System.out.print(cell.renderValue() + " ");
            }
            System.out.println();
        }
        renderMessage("");
    }

    @Override
    public void renderMessage(String message) {
        System.out.println(message);
    }

    @Override
    public boolean renderPlayAgain() {
        renderMessage("Press any key to play again...");
        scanner.nextLine();
        return true;
    }
}
