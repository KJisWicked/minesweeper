package minesweeper.model;

public class GameState {
    private final Grid grid;
    private boolean isGameOver;
    private boolean isGameWon;
    private int revealedCells;
    private final int totalNonMineCells;

    public GameState(Grid grid) {
        this.grid = grid;
        this.isGameOver = false;
        this.isGameWon = false;
        this.revealedCells = 0;
        this.totalNonMineCells = grid.getTotalNonMineCells();
    }

    public Grid getGrid() {
        return grid;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public int getRevealedCells() {
        return revealedCells;
    }

    public void setGameOver(boolean gameOver) {
        this.isGameOver = gameOver;
    }

    public void setGameWon(boolean gameWon) {
        this.isGameWon = gameWon;
        if (gameWon) {
            this.isGameOver = true;
        }
    }

    public void incrementRevealedCells() {
        this.revealedCells++;
    }

    public void checkWinCondition() {
        if (revealedCells == totalNonMineCells) {
            setGameWon(true);
        }
    }
}
