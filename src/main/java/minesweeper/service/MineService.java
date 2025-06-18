package minesweeper.service;

import minesweeper.model.Cell;
import minesweeper.model.Grid;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MineService {
    private final Random random;
    private static final double MAX_MINE_RATIO = 0.35;

    public MineService() {
        this.random = new Random();
    }

    // For Junit Test Case Only
    public MineService(long seed) {
        this.random = new Random(seed);
    }

    public void placeMines(Grid grid, int totalMines) {
        Set<String> minePositions = new HashSet<>();
        int gridSize = grid.getSize();

        while (minePositions.size() < totalMines) {
            int row = random.nextInt(gridSize);
            int column = random.nextInt(gridSize);
            String position = row + "," + column;

            if (minePositions.add(position)) {
                Cell cell = grid.getCell(row, column);
                cell.setMine(true);
            }
        }
    }

    public int calculateMaxMines(int gridSize) {
        int totalCells = gridSize * gridSize;
        return (int) Math.floor(totalCells * MAX_MINE_RATIO);
    }
}
