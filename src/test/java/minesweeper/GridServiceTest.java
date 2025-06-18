package minesweeper;

import minesweeper.model.Cell;
import minesweeper.model.Grid;
import minesweeper.service.GridService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridServiceTest {
    private GridService gridService;
    private Grid grid;

    @BeforeEach
    void setUp() {
        gridService = new GridService();
        grid = new Grid(3, 1);
    }

    @Test
    @DisplayName("Test Reveal Single Cell")
    void testRevealCell_SingleCellReveal() {
        Cell cell = grid.getCell(0, 0);
        cell.setAdjacentMineCount(2);

        int revealedCount = gridService.revealCell(grid, 0, 0);

        assertEquals(1, revealedCount);
        assertTrue(cell.isRevealed());
    }

    @Test
    @DisplayName("Test No Duplicate Reveal of Revealed Cell")
    void testRevealCell_NoDuplicateCellReveal() {
        Cell cell = grid.getCell(0, 0);
        cell.setRevealed(true);

        int revealedCount = gridService.revealCell(grid, 0, 0);

        assertEquals(0, revealedCount);
    }

    @Test
    @DisplayName("Test Reveal of Invalid Coordinate (Non Existent Cell)")
    void testRevealCell_InvalidCoordinate() {
        int revealedCount = gridService.revealCell(grid, -1, 0); // Invalid Row
        assertEquals(0, revealedCount);
    }

    @Test
    @DisplayName("Test Calculate Adjacent Mine Counts")
    void testCalculateAdjacentMineCounts() {
        grid.getCell(0, 0).setMine(true);

        gridService.calculateAdjacentMineCounts(grid);

        assertEquals(1, grid.getCell(0, 1).getAdjacentMineCount());
        assertEquals(1, grid.getCell(1, 0).getAdjacentMineCount());
        assertEquals(1, grid.getCell(1, 1).getAdjacentMineCount());
    }

    @Test
    @DisplayName("Test Flood Fill ALgorithm")
    void testFloodFill() {
        final int GRID_SIZE = 3;
        final int TOTAL_MINES = 0;

        // Create a grid with no mines for flood fill
        Grid grid = new Grid(GRID_SIZE, TOTAL_MINES);
        gridService.calculateAdjacentMineCounts(grid);

        int revealedCount = gridService.revealCell(grid, 1, 1);

        assertEquals(GRID_SIZE * GRID_SIZE, revealedCount);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                assertTrue(grid.getCell(row, column).isRevealed());
            }
        }
    }
}
