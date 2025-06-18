package minesweeper;

import minesweeper.model.Cell;
import minesweeper.model.GameState;
import minesweeper.model.Grid;
import minesweeper.service.GameService;
import minesweeper.service.GridService;
import minesweeper.service.MineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServiceTest {
    private static final int MIN_GRID_SIZE = 3;
    private static final int MEDIUM_GRID_SIZE = 5;
    private static final int MAX_GRID_SIZE = 10;
    private static final int NO_MINES = 0;
    private static final int FEW_MINES = 2;
    private static final int MANY_MINES = 10;

    @Mock
    private GridService mockGridService;

    @Mock
    private MineService mockMineService;

    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gameService = new GameService(mockGridService, mockMineService);
    }

    @Test
    @DisplayName("Initialize Game - Valid Grid Size & Mine Count")
    void testInitializeGame_ValidParameters() {
        // Given
        when(mockMineService.calculateMaxMines(MIN_GRID_SIZE)).thenReturn(3);

        // When
        GameState gameState = gameService.initializeGame(MIN_GRID_SIZE, FEW_MINES);

        // Then
        assertNotNull(gameState);
        assertNotNull(gameState.getGrid());
        assertEquals(MIN_GRID_SIZE, gameState.getGrid().getSize());
        assertEquals(FEW_MINES, gameState.getGrid().getTotalMines());
        assertFalse(gameState.isGameOver());
        assertFalse(gameState.isGameWon());
        assertEquals(0, gameState.getRevealedCells());

        // Verify Service Calls
        verify(mockMineService).placeMines(any(Grid.class), eq(FEW_MINES));
        verify(mockGridService).calculateAdjacentMineCounts(any(Grid.class));
    }

    @Test
    @DisplayName("Return Correct Max Mines for Given Grid Size")
    void testReturnCorrectMaxMines() {
        int expectedMaxMines = 3;
        when(mockMineService.calculateMaxMines(MIN_GRID_SIZE)).thenReturn(expectedMaxMines);

        int actualMaxMines = gameService.getAllowedMaxMines(MIN_GRID_SIZE);

        assertEquals(expectedMaxMines, actualMaxMines);
        // Verify GameService actually called MineService's calculateMaxMines method.
        verify(mockMineService).calculateMaxMines(MIN_GRID_SIZE);
    }

    @Test
    @DisplayName("Return Error Message for Revealed Cell")
    void testReturnErrorMessageForRevealedCell() {
        // Given
        Grid grid = new Grid(MIN_GRID_SIZE, NO_MINES);
        GameState gameState = new GameState(grid);
        Cell cell = grid.getCell(0, 0);
        cell.setRevealed(true);

        // When
        String result = gameService.processMove(gameState, 0, 0);

        // Then
        assertEquals("Cell already revealed!", result);
    }

    @Test
    @DisplayName("Return Error Message for Invalid Position")
    void testReturnErrorMessageForInvalidPosition() {
        // Given
        Grid grid = new Grid(MIN_GRID_SIZE, NO_MINES);
        GameState gameState = new GameState(grid);

        // When
        String result = gameService.processMove(gameState, 10, 10);

        // Then
        assertEquals("Invalid Position!", result);
    }

    @Test
    @DisplayName("Win Game when All Non-Mine Cells Revealed")
    void testWinGameWhenAllNonMinesRevealed() {
        // Given (3 x 3 Grid, with 1 mine) - 8 non-mine cells
        // In actual there is no mine as we do not run placeMines(), this is just a simulation.
        Grid grid = new Grid(MIN_GRID_SIZE, 1);
        GameState gameState = new GameState(grid);

        // Simulate 7 cells already revealed with 1 more needed to win.
        for (int cell = 0; cell < 7; cell++) {
            gameState.incrementRevealedCells();
        }

        Cell cell = grid.getCell(1, 1);
        cell.setAdjacentMineCount(1);

        when(mockGridService.revealCell(grid, 1, 1)).thenReturn(1);

        // When
        String result = gameService.processMove(gameState, 1, 1);

        // Then
        assertEquals("Congratulations, you have won the game!", result);
        assertTrue(gameState.isGameWon());
        assertTrue(gameState.isGameOver());
        assertEquals(8, gameState.getRevealedCells());
    }

    @Test
    @DisplayName("End Game when Mine is Detonated")
    void testEndGameWhenMineIsDetonated() {
        // Given: We set the mine at [1,1] position, given a 3 x 3 grid.
        Grid grid = new Grid(MIN_GRID_SIZE, 1);
        GameState gameState = new GameState(grid);
        Cell cell = grid.getCell(1, 1);
        cell.setMine(true);

        // When
        String result = gameService.processMove(gameState, 1, 1);

        // Then
        assertTrue(cell.isRevealed());
        assertEquals("Oh no, you detonated a mine! Game over.", result);
        assertTrue(gameState.isGameOver());
        assertFalse(gameState.isGameWon());
    }
}
