package minesweeper;

import minesweeper.model.Grid;
import minesweeper.service.MineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MineServiceTest {
    private MineService mineService;

    @BeforeEach
    void setUp() {
        mineService = new MineService(12345L);
    }

    @Test
    @DisplayName("Test Place Mines with Valid Mine Count")
    void testPlaceMines_ValidMineCount() {
        final int GRID_SIZE = 4;
        final int TOTAL_MINES = 2;

        Grid grid = new Grid(GRID_SIZE, TOTAL_MINES);

        mineService.placeMines(grid, TOTAL_MINES);

        int mineCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                if (grid.getCell(row, column).isMine()) {
                    mineCount++;
                }
            }
        }

        assertEquals(TOTAL_MINES, mineCount);
    }

    @Test
    @DisplayName("Test Place Mines with No Duplicates (Unique Cells)")
    void testPlaceMines_NoDuplicates() {
        // To ensure mines are placed uniquely in all cells and no duplicates (less mines than intended).
        final int GRID_SIZE = 3;
        final int TOTAL_MINES = 9;

        Grid grid = new Grid(GRID_SIZE, TOTAL_MINES);

        mineService.placeMines(grid, TOTAL_MINES);

        int mineCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                if (grid.getCell(row, column).isMine()) {
                    mineCount++;
                }
            }
        }

        assertEquals(TOTAL_MINES, mineCount);
    }

    @Test
    @DisplayName("Test Calculate Max Mines - 35% of Grid Size")
    void testCalculateMaxMines() {
        // NOTE:
        // MAX_MINE_RATIO is 0.35 (35% of total of cells).
        // MIN_GRID_SIZE = 3
        // MAX_GRID_SIZE = 20
        assertEquals(3, mineService.calculateMaxMines(3));
        assertEquals(28, mineService.calculateMaxMines(9));
        assertEquals(140, mineService.calculateMaxMines(20));
    }

    @Test
    @DisplayName("Test Place Zero Mines")
    void testPlaceMines_ZeroMines() {
        final int GRID_SIZE = 3;
        final int TOTAL_MINES = 0;

        Grid grid = new Grid(GRID_SIZE, TOTAL_MINES);
        mineService.placeMines(grid, TOTAL_MINES);

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                assertFalse(grid.getCell(row, column).isMine());
            }
        }
    }
}
