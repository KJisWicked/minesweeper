package minesweeper.service;

import minesweeper.model.Cell;
import minesweeper.model.GameState;
import minesweeper.model.Grid;

public class GameService {
    private final GridService gridService;
    private final MineService mineService;

    public GameService(GridService gridService, MineService mineService) {
        this.gridService = gridService;
        this.mineService = mineService;
    }

    public GameState initializeGame(int gridSize, int totalMines) {
        Grid grid = new Grid(gridSize, totalMines);
        mineService.placeMines(grid, totalMines);
        gridService.calculateAdjacentMineCounts(grid);
        return new GameState(grid);
    }

    public int getAllowedMaxMines(int gridSize) {
        return mineService.calculateMaxMines(gridSize);
    }

    public String processMove(GameState gameState, int row, int column) {
        Grid grid = gameState.getGrid();
        Cell cell = grid.getCell(row, column);

        if (cell == null) {
            return "Invalid Position!";
        }

        if (cell.isRevealed()) {
            return "Cell already revealed!";
        }

        if (cell.isMine()) {
            cell.setRevealed(true);
            gameState.setGameOver(true);
            return "Oh no, you detonated a mine! Game over.";
        }

        int revealCount = gridService.revealCell(grid, row, column);
        for (int i = 0; i < revealCount; i++) {
            gameState.incrementRevealedCells();
        }

        gameState.checkWinCondition();

        if (gameState.isGameWon()) {
            return "Congratulations, you have won the game!";
        }

        return String.format("This square contains %d adjacent mines.", cell.getAdjacentMineCount());
    }
}
