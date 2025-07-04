package minesweeper.service;

import minesweeper.model.Cell;
import minesweeper.model.Grid;

import java.util.LinkedList;
import java.util.Queue;

public class GridService {
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    public void calculateAdjacentMineCounts(Grid grid) {
        for (int row = 0; row < grid.getSize(); row++) {
            for (int column = 0; column < grid.getSize(); column++) {
                Cell cell = grid.getCell(row, column);
                if (!cell.isMine()) {
                    int mineCount = countAdjacentMines(grid, row, column);
                    cell.setAdjacentMineCount(mineCount);
                }
            }
        }
    }

    private int countAdjacentMines(Grid grid, int row, int column) {
        int count = 0;
        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];
            Cell cell = grid.getCell(newRow, newColumn);
            if (cell != null && cell.isMine()) {
                count++;
            }
        }

        return count;
    }

    public int revealCell(Grid grid, int row, int column) {
        Cell cell = grid.getCell(row, column);
        if (cell == null || cell.isRevealed()) {
            return 0;
        }

        cell.setRevealed(true);
        int revealCount = 1;

        // If cell has no adjacent mines, reveal all adjacent cells via flood fill.
        if (!cell.isMine() && cell.getAdjacentMineCount() == 0) {
            revealCount += floodFill(grid, row, column);
        }

        return revealCount;
    }
    
    private int floodFill(Grid grid, int startRow, int startColumn) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startColumn});
        int revealCount = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int column = current[1];

            for (int[] direction : DIRECTIONS) {
                int newRow = row + direction[0];
                int newColumn = column + direction[1];

                Cell cell = grid.getCell(newRow, newColumn);
                if (cell != null && !cell.isRevealed() && !cell.isMine()) {
                    cell.setRevealed(true);
                    revealCount++;

                    if (cell.getAdjacentMineCount() == 0) {
                        queue.offer(new int[]{newRow, newColumn});
                    }
                }
            }
        }

        return revealCount;
    }
}
