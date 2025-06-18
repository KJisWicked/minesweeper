package minesweeper.model;

public class Grid {
    private final int size;
    private final int totalMines;
    private final Cell[][] cells;

    public Grid(int size, int totalMines) {
        this.size = size;
        this.totalMines = totalMines;
        this.cells = new Cell[size][size];
        initializeCells();
    }

    private void initializeCells() {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                cells[row][column] = new Cell(row, column);
            }
        }
    }

    public Cell getCell(int row, int column) {
        if (isValidPosition(row, column)) {
            return cells[row][column];
        }

        return null;
    }

    public boolean isValidPosition(int row, int column) {
        return row >= 0 && row < size && column >= 0 && column < size;
    }

    public int getSize() {
        return size;
    }

    public int getTotalCells() {
        return size * size;
    }

    public int getTotalNonMineCells() {
        return getTotalCells() - totalMines;
    }

    public int getTotalMines() {
        return totalMines;
    }
}
