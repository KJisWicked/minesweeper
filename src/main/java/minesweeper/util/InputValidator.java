package minesweeper.util;

public class InputValidator {

    private static final int MIN_GRID_SIZE = 3;
    private static final int MAX_GRID_SIZE = 20;

    public boolean isValidGridSize(String input) {
        try {
            int size = Integer.parseInt(input);
            return size >= MIN_GRID_SIZE && size <= MAX_GRID_SIZE;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Conditions:
     * - At least minimum of 1 mine to be solvable
     * - Not more than 35% of total squares
     */
    public boolean isValidMineCount(String input, int gridSize, int maxMines) {
        try {
            int mines = Integer.parseInt(input);
            return mines > 0 && mines <= maxMines;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public int[] parseCoordinates(String input, int gridSize) {
        if (input == null || input.length() < 2) {
            return null;
        }

        try {
            char rowCharacter = Character.toUpperCase(input.charAt(0));
            String columnString = input.substring(1);

            if (rowCharacter < 'A' || rowCharacter > 'Z') {
                return null;
            }

            int row = rowCharacter - 'A';
            int column = Integer.parseInt(columnString) - 1;

            if (row < gridSize && column >= 0 && column < gridSize) {
                return new int[]{row, column};
            }
        } catch (NumberFormatException exception) {
            return null;
        }

        return null;
    }
}
